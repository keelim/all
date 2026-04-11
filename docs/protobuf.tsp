// tsp format .
// tsp compile protobuf.tsp --emit=@typespec/protobuf
import "@typespec/http";
import "@typespec/protobuf";

using TypeSpec.Http;
using TypeSpec.Protobuf;

@package({
  name: "keelim",
})
namespace Keelim;

enum ThemeType {
  THEME_TYPE_UNSPECIFIED: 0,
  THEME_TYPE_LIGHT: 1,
  THEME_TYPE_DARK: 2,
}

model UserSetting {
  @field(1) is_first_user: boolean;
  @field(2) visited_time: int32;
  @field(3) theme_type: ThemeType;
}
