"""Generate explicit public test configuration for non-deployment build validation."""
import json
from pathlib import Path

root = Path(__file__).resolve().parents[2]
properties = {
    'MAPS_API_KEY': '', 'GENERATIVE': '', 'SHEET_KEY': '',
    'NOTIFICATION_URL': 'https://example.invalid',
    'AD_COMMON_BANNER_DEBUG_ID': 'ca-app-pub-3940256099942544/6300978111',
    'AD_ARDUCON_BANNER_ID': 'ca-app-pub-3940256099942544/6300978111',
    'AD_NANDA_APPLICATION_ID': 'ca-app-pub-3940256099942544~3347511713',
    'AD_NANDA_BANNER_ID': 'ca-app-pub-3940256099942544/6300978111',
    'AD_NANDA_TEST_BANNER_ID': 'ca-app-pub-3940256099942544/6300978111',
    'ARDUON_APPLICATION_ID': 'ca-app-pub-3940256099942544~3347511713',
    'APPLICATION_ID': 'ca-app-pub-3940256099942544~3347511713',
    'COMSSA_APPLICATION_ID': 'ca-app-pub-3940256099942544~3347511713',
    'MYGRADE_APPLICATION_ID': 'ca-app-pub-3940256099942544~3347511713',
}
content = '# Public/empty CI fixtures only; never deploy these values.\n' + ''.join(
    f'{key}={json.dumps(value)}\n' for key, value in properties.items()
)
for name in ['local.defaults.properties', 'local.properties']:
    path = root / name
    if path.exists():
        raise SystemExit(f'Refusing to overwrite existing {name}')
    path.write_text(content)
for app, package in {'arducon': 'com.keelim.arducon', 'cnubus': 'com.keelim.cnubus',
                     'comssa': 'com.keelim.comssa', 'my-grade': 'com.keelim.mygrade'}.items():
    path = root / f'app-{app}/google-services.json'
    if path.exists():
        raise SystemExit(f'Refusing to overwrite existing {path.relative_to(root)}')
    path.write_text(json.dumps({
        'project_info': {'project_number': '1234567890', 'project_id': 'offline-build-fixture'},
        'client': [{'client_info': {'mobilesdk_app_id': '1:1234567890:android:0000000000000000',
                                  'android_client_info': {'package_name': package}},
                    'api_key': [{'current_key': 'offline-placeholder-not-a-provider-key'}]}],
        'configuration_version': '1',
    }))
print('Generated public offline build fixtures; existing files were not overwritten')
