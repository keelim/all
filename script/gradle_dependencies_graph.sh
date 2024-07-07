./gradlew generateModulesGraphvizText -Pmodules.graph.output.gv=app-arducon.gv -Pmodules.graph.of.module=:app-arducon
./gradlew generateModulesGraphvizText -Pmodules.graph.output.gv=app-cnubus.gv -Pmodules.graph.of.module=:app-cnubus
./gradlew generateModulesGraphvizText -Pmodules.graph.output.gv=app-comssa.gv -Pmodules.graph.of.module=:app-comssa
./gradlew generateModulesGraphvizText -Pmodules.graph.output.gv=app-my-grade.gv -Pmodules.graph.of.module=:app-my-grade
./gradlew generateModulesGraphvizText -Pmodules.graph.output.gv=app-nanda.gv -Pmodules.graph.of.module=:app-nanda

dot -Tsvg app-arducon.gv > ./app-arducon/app-arducon.svg
dot -Tsvg app-cnubus.gv > ./app-cnubus/app-cnubus.svg
dot -Tsvg app-comssa.gv > ./app-comssa/app-comssa.svg
dot -Tsvg app-my-grade.gv > ./app-my-grade/app-my-grade.svg
dot -Tsvg app-nanda.gv > ./app-nanda/app-nanda.svg
