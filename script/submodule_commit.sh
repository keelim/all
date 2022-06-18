git submodule foreach git checkout -b newfeature
git submodule foreach git add -A .
git submodule foreach git commit -am "update common module"
git submodule foreach git checkout master
git submodule foreach git merge newfeature
git submodule foreach git push
git submodule foreach git branch -d newfeature
