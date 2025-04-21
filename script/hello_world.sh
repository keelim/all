RESULT="Hello World"

echo "$RESULT" | sed ':a;N;$!ba;s/\n/\\n/g'
