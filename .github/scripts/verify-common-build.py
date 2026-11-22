"""Check the SDK, generated AppFunctions contracts, runner discovery and packaged services."""
import os
from pathlib import Path
import sys
import tomllib
import xml.etree.ElementTree as ET
import zipfile

ROOT = Path(__file__).resolve().parents[2]
SHA = '971fcd7150ca4ba04852c0b2ca0d2d0ab9468e14b03473eab19528809914c76b'
CONTRACTS = {
    'json_formatter_app_functions.xml': ('com.keelim.appfunction.json.JsonFormatterAppFunctions#formatJson', 'com.keelim.appfunction.json.JsonFormatterAppFunctionService'),
    'health_app_functions.xml': ('com.keelim.appfunction.health.HealthAppFunctions#getHealthStatus', 'com.keelim.appfunction.health.HealthAppFunctionService'),
}
ANDROID = '{http://schemas.android.com/apk/res/android}'


def contract(data, filename):
    root = ET.fromstring(data)
    ids = [node.text for node in root.iter('functionId')]
    assert ids == [CONTRACTS[filename][0]], (filename, ids)
    text = ET.tostring(root, encoding='unicode')
    assert 'AppFunctionContext' not in text
    if filename.startswith('json'):
        assert 'inputJson' in text
    else:
        for field in ('status', 'module', 'epochMillis'):
            assert field in text, (filename, field)
    descriptions = [node.text for node in root.iter('description') if node.text]
    assert descriptions, f'Missing generated KDoc descriptions: {filename}'


def configuration():
    versions = tomllib.loads((ROOT / 'gradle/libs.versions.toml').read_text())['versions']
    assert versions['compileSdk'] == '37' and versions['compileSdkMinor'] == '2'
    conventions = ROOT / 'build-logic/convention/src/main/kotlin'
    android_config = (conventions / 'com/keelim/builds/KotlinAndroid.kt').read_text()
    android, multiplatform = android_config.split('fun Project.configureMultiplatformAndroid(', maxsplit=1)
    sdk_minor_lookup = 'findVersion("compileSdkMinor")'
    assert sdk_minor_lookup in android and sdk_minor_lookup in multiplatform
    for name in ('KeelimMultiPlatformConventionPlugin.kt', 'KeelimMultiplatformLibraryPlugin.kt'):
        assert 'configureMultiplatformAndroid(this)' in (conventions / name).read_text()
    for path in ('gradle/wrapper/gradle-wrapper.properties', 'build-logic/gradle/wrapper/gradle-wrapper.properties'):
        text = (ROOT / path).read_text()
        assert 'gradle-9.9.2-bin.zip' in text and f'distributionSha256Sum={SHA}' in text
    sdk = Path(os.environ.get('ANDROID_HOME') or os.environ['ANDROID_SDK_ROOT'])
    assert (sdk / 'platforms/android-37.2/android.jar').is_file(), 'Exact SDK 37.2 was not installed'
    print('SDK 37.2 installed. Android conventions and Gradle 9.9.2 checksums agree.')


def appfunctions():
    generated = ROOT / 'feature/app-function/impl/build/generated/ksp/debug'
    for filename in CONTRACTS:
        paths = list(generated.rglob(filename))
        assert paths, f'No generated schema: {filename}'
        for path in paths:
            contract(path.read_bytes(), filename)
    reports = list((ROOT / 'feature/app-function/impl/build/test-results/testDebugUnitTest').glob('TEST-*.xml'))
    assert reports, 'No AppFunctions test reports'
    suites = [ET.parse(path).getroot() for path in reports]
    assert all(int(s.get('failures', 0)) == 0 and int(s.get('errors', 0)) == 0 for s in suites)
    cases = [case for s in suites for case in s.iter('testcase')]
    assert len(cases) >= 6, f'Expected provider + API 36 service tests, got {len(cases)}'
    for name in ('JsonFormatterAppFunctionsTest', 'HealthAppFunctionServiceTest'):
        assert any(name in case.get('classname', '') for case in cases), f'Vintage did not discover {name}'
    assert not any(list(case.iter('skipped')) for case in cases), 'AppFunctions tests must execute'
    print(f'Generated IDs/models verified; {len(cases)} tests passed, including Java 21 / API 36 service assertions')


def consumer(app):
    for app in (app,):
        apks = list((ROOT / app / 'build/outputs/apk/debug').glob('*.apk'))
        assert apks, f'No debug APK for {app}'
        for apk in apks:
            with zipfile.ZipFile(apk) as archive:
                for filename in CONTRACTS:
                    contract(archive.read('assets/' + filename), filename)
                assert 'assets/app_functions_schema.xsd' in archive.namelist()
        manifests = list((ROOT / app / 'build/intermediates').glob('merged_manifest*/debug/**/AndroidManifest.xml'))
        assert manifests, f'No merged debug manifest for {app}'
        for path in manifests:
            manifest = ET.parse(path).getroot()
            for filename, (_, service_name) in CONTRACTS.items():
                services = [s for s in manifest.iter('service') if s.get(ANDROID + 'name') == service_name]
                assert len(services) == 1, (app, service_name)
                service = services[0]
                assert service.get(ANDROID + 'exported') == 'true'
                assert service.get(ANDROID + 'permission') == 'android.permission.BIND_APP_FUNCTION_SERVICE'
                assert any(a.get(ANDROID + 'name') == 'android.app.appfunctions.AppFunctionService' for a in service.iter('action'))
                assert any(p.get(ANDROID + 'name') == 'android.app.appfunctions.v2' and p.get(ANDROID + 'value') == filename for p in service.iter('property'))
        print(f'{app}: debug APK contracts and protected merged services verified')


def consumers():
    failures = []
    for app in ('app-arducon', 'app-cnubus', 'app-comssa', 'app-my-grade'):
        try:
            consumer(app)
        except (AssertionError, KeyError, ET.ParseError) as error:
            failures.append(f'{app}: {error}')
    assert not failures, '\n'.join(failures)


{'configuration': configuration, 'appfunctions': appfunctions, 'consumers': consumers}[sys.argv[1]]()
