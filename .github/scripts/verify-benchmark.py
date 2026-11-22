import json
import re
import subprocess
import tempfile
from html.parser import HTMLParser
from pathlib import Path
from urllib.request import urlopen

class Assets(HTMLParser):
    def __init__(self):
        super().__init__()
        self.urls = []
    def handle_starttag(self, tag, attrs):
        attrs = dict(attrs)
        value = attrs.get("src") if tag == "script" else attrs.get("href", "")
        if value:
            self.urls.append(value)

html = Path("profile/benchmark.html").read_text()
assets = Assets()
assets.feed(html)
font = next(url for url in assets.urls if "/font-awesome/" in url)
math = next(url for url in assets.urls if "/mathjs/" in url)
with tempfile.TemporaryDirectory() as temp:
    root = Path(temp)
    for index, script in enumerate(re.findall(r"<script>(.*?)</script>", html, re.S)):
        path = root / f"inline-{index}.js"
        path.write_text(script)
        subprocess.run(["node", "--check", str(path)], check=True)
    css = urlopen(font, timeout=30).read().decode()
    assert "font-family" in css and "woff2" in css
    # Check the referenced webfont as well as the stylesheet.
    from urllib.parse import urljoin
    webfont = re.search(r"url\(([^)]+\.woff2)\)", css).group(1).strip("\"'")
    assert len(urlopen(urljoin(font, webfont), timeout=30).read()) > 1000
    bundle = root / "math.cjs"
    bundle.write_bytes(urlopen(math, timeout=30).read())
    test = root / "test.cjs"
    test.write_text("const assert = require('node:assert/strict'); const math = require('./math.cjs'); "
                    "assert.equal(math.erf(0), 0); assert.equal(math.sqrt(4), 2); "
                    "assert.equal(0.5 * (1 + math.erf(0 / math.sqrt(2))), 0.5); "
                    "assert.ok(Math.abs(0.5 * (1 + math.erf(1.96 / math.sqrt(2))) - 0.975) < 0.0001);")
    subprocess.run(["node", str(test)], check=True)
print(json.dumps({"font": font, "math": math, "inline_syntax": "passed", "statistical_api": "passed"}))
