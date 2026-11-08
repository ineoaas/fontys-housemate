#!/usr/bin/env bash
# Prints the current public link of the live site.
# The Cloudflare quick tunnel gets a new link every time it restarts, so look it up here.
# Usage: ./link.sh   (works from any folder)

cd "$(dirname "$0")" || exit 1

if [ -z "$(docker compose ps -q --status running cloudflared)" ]; then
  echo "The tunnel isn't running. Start the site with: docker compose up -d (in the deploy folder)" >&2
  exit 1
fi

link=$(docker compose logs cloudflared 2>&1 | grep -o 'https://[a-z0-9-]*\.trycloudflare\.com' | tail -1)

if [ -z "$link" ]; then
  echo "The tunnel is still starting. Try again in a few seconds." >&2
  exit 1
fi

echo "$link"
