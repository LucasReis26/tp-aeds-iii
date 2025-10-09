#!/usr/bin/env bash
# Run from project root: /home/theokiwi/Documents/Faculdade/4st/AEDS3/tp-aeds-iii
set -e

# Ensure clean git working tree
if ! git diff-index --quiet HEAD --; then
  echo "Working tree not clean. Commit or stash changes first." >&2
  exit 1
fi

# Paths
BASE="src/main/java/com/example/tpaedsiii/repository"

# Move BD subpackages to lowercase bd/* (if present)
if [ -d "$BASE/BD/base" ]; then
  mkdir -p "$BASE/bd/base"
  git mv "$BASE/BD/base" "$BASE/bd/" || true
fi

if [ -d "$BASE/BD/Filme" ]; then
  mkdir -p "$BASE/bd/filme"
  git mv "$BASE/BD/Filme" "$BASE/bd/" || true
fi

if [ -d "$BASE/BD/Lista" ]; then
  mkdir -p "$BASE/bd/lista"
  git mv "$BASE/BD/Lista" "$BASE/bd/" || true
fi

if [ -d "$BASE/BD/User" ]; then
  mkdir -p "$BASE/bd/user"
  git mv "$BASE/BD/User" "$BASE/bd/" || true
fi

# Remove empty BD dir if exists
if [ -d "$BASE/BD" ]; then
  rmdir "$BASE/BD" || true
fi

# Move DAO -> dao
if [ -d "$BASE/DAO" ]; then
  mkdir -p "$BASE/dao"
  git mv "$BASE/DAO" "$BASE/dao" || true
fi

# Normalize package/import declarations inside Java files
# Replace com.example.tpaedsiii.repository.BD.* -> ...bd.*
# Replace com.example.tpaedsiii.repository.DAO -> ...dao
find src/main/java -name "*.java" -print0 | xargs -0 sed -i \
  -e 's/package com\.example\.tpaedsiii\.repository\.BD\./package com.example.tpaedsiii.repository.bd./g' \
  -e 's/import com\.example\.tpaedsiii\.repository\.BD\./import com.example.tpaedsiii.repository.bd./g' \
  -e 's/package com\.example\.tpaedsiii\.repository\.DAO/package com.example.tpaedsiii.repository.dao/g' \
  -e 's/import com\.example\.tpaedsiii\.repository\.DAO/import com.example.tpaedsiii.repository.dao/g' \
  -e 's/package com\.example\.tpaedsiii\.repository\.BD/package com.example.tpaedsiii.repository.bd/g' \
  -e 's/import com\.example\.tpaedsiii\.repository\.BD/import com.example.tpaedsiii.repository.bd/g'

# Show changes and build
git add -A
echo "Files staged for commit. Inspect with 'git status' and 'git diff --staged'."
echo "To commit: git commit -m 'Normalize package names to lowercase (bd, dao) and update imports'"

# Optional: run mvn build (uncomment if you want the script to build)
# mvn -q -DskipTests clean package

echo "Done."
