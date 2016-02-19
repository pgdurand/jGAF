#!/bin/bash

LICENCEFILE="license_include.txt"

[ ! -f "$LICENCEFILE" ] && echo "$LICENCEFILE is missing. Abort." && exit 1

for i in $1/*.java; do
    [ "$(head -c16 $i)" == "/* Copyright (C)" ] && continue

    NEWFILE="${i}.new"
    [ -f "$NEWFILE" ] && echo "Sorry, $NEWFILE already exists" && continue

	#echo "$NEWFILE"
    cat "$LICENCEFILE" > "$NEWFILE"
    cat "$i" >> "$NEWFILE"
    rm -f "$i"
    mv "$NEWFILE" "$i"
done
