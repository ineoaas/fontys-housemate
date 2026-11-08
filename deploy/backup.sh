#!/usr/bin/env bash
# Saves a backup of the live database to deploy/backups/ (git ignores that folder).
# Keeps the newest 14 backups and deletes older ones.
# Usage: ./backup.sh   (works from any folder; the site must be running)

# Restore a backup (this REPLACES everything in the live database):
#   gunzip -c backups/<file>.sql.gz | docker compose exec -T live-database psql -U housemate -d housemate

set -euo pipefail
cd "$(dirname "$0")"

keep=14
mkdir -p backups
file="backups/housemate-$(date +%Y-%m-%d_%H-%M-%S).sql.gz"

# --clean: the backup first drops the old tables, so restoring gives exactly this state
if ! docker compose exec -T live-database pg_dump -U housemate -d housemate --clean --if-exists | gzip > "$file"; then
  rm -f "$file"
  echo "Backup failed. Is the site running? Start it with: docker compose up -d" >&2
  exit 1
fi

echo "Saved $file ($(du -h "$file" | cut -f1))"

# Delete everything except the newest $keep backups
ls -1t backups/housemate-*.sql.gz | tail -n +$((keep + 1)) | xargs -r rm --
