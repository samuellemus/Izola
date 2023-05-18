#!/bin/bash
git add .
git commit -m "updated with script"
git push origin main
touch test.txt
rm -rf test.txt
git add .
git commit -m "updated with script"
git push origin main
