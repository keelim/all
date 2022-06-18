git submodule foreach git checkout master
git submodule foreach git add -A .
git submodule foreach git commit -am "update common module"
git submodule foreach git push
