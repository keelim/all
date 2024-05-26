// tsp compile openapi.tsp --emit=@typespec/openapi3
// npx @openapitools/openapi-generator-cli generate -i raw/raw.yaml -g html2 -o
// current code is example

import "@typespec/http";
import "@typespec/openapi3";

using TypeSpec.Http;
@service({
  title: "App Interface",
})
namespace Keelim;

model DarkMode {
  isDarkMode: boolean;
  mode: "light" | "dark" | "system";
}

@error
model Error {
  statusCode: int32;
  message: string;
}

@route("/")
interface App {
  @route("isDarkMode") @get() isDarkMode(): DarkMode | Error;
}
