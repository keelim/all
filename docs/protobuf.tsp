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

model UserSetting {
  @field(1) should_hide_onboarding: boolean;
  @field(2) last_visit_time: string;
}
