#!/bin/bash

echo "📦 What changed? Enter commit message:"
read -r msg

if [ -z "$msg" ]; then
  msg="Update — $(date '+%Y-%m-%d %H:%M')"
fi

git add .
git commit -m "$msg"
git push origin main

echo "✅ Pushed to GitHub!"
