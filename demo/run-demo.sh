#!/usr/bin/env bash
# Demo with head-on avoidance on B-C, a convoy on A-B and cars queueing at level crossings.
set -e
cd "$(dirname "$0")/.."

mkdir -p bin
javac -d bin $(find src -name "*.java")

java -Dsimulation.config=demo/configuration.txt -Dsimulation.autostart=true -cp bin org.unibl.etf.pj2.Simulation &
SIM=$!
trap 'kill $SIM 2>/dev/null || true' EXIT INT

drop() { cp "demo/$1" data/.stage && mv data/.stage "data/TRAINS/$1"; }

WARMUP=${1:-15}
for ((i = WARMUP; i > 0; i--)); do
	echo "Trains start in $i s..."
	sleep 1
done

drop 01-head-on-B-to-C.txt
drop 02-head-on-C-to-B.txt
drop 03-convoy-leader-A-to-B.txt
sleep 6
drop 04-convoy-follower-A-to-B.txt

echo "Demo is running. Press Ctrl+C to stop."
wait $SIM
