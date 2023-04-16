# Pre-commit

## Install

```Shell
brew install pre-commit
pre-commit install
```

## Project Setting

- create file (`.pre-commit-config.yaml`)

```yaml
repos:
    -   repo: https://github.com/pre-commit/pre-commit-hooks
        rev: v2.4.0
        hooks:
            -   id: check-json
            -   id: check-xml
            -   id: check-toml
            -   id: check-merge-conflict
            -   id: detect-private-key
            -   id: no-commit-to-branch

#    -   repo: https://github.com/jguttman94/pre-commit-gradle
#        rev: v0.3.0
#        hooks:
#            -   id: gradle-spotless
#                args: [ '-w', --wrapper ]

```
