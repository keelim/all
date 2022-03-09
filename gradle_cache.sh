#gradle cache 문제를 해결하기 위한 스크립트 (21일 이후 안쓰는 캐시는 지우기)
alias killgradlecache='find ~/.gradle -type f -atime +21 -delete && find ~/.gradle -mindepth 1 -type d -empty -delete'
du -sh ~/.gradle
killgradlecache
du -sh ~/.gradle