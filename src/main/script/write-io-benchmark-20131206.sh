#!/bin/bash

# common configuration
dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
jar="$dir/../../../target/micro-benchmarks-bin.jar"
cls="eu.stratosphere.microbenchmark.nio.NIOWriteBenchmarkPersisted"
mem=2048M

# jUnit Benchmarks configuration
jub_db_file="$dir/../../../data/benchmarks"
jub_charts_dir="$dir/../../../data/charts/."

# sanity check
if ! [ -e "$dir/../../../target/micro-benchmarks-bin.jar" ]; then
	echo "Artifact micro-benchmarks-bin.jar does not exist. Please run 'mvn package' before this script!"
	exit -1
fi

# benchmark running routine
function run_benchmark {
	java \
		-Xms${mem}                         \
		-Xmx${mem}                         \
		-Djub.db.file=${jub_db_file}       \
		-Djub.charts.dir=${jub_charts_dir} \
		-Djub.consumers=CONSOLE,H2         \
		-Djub.customkey=$1                 \
		-cp ${jar}                         \
		org.junit.runner.JUnitCore ${cls}
}

###############################################################################
# 25MB file
###############################################################################
# 5 times for managed buffers
run_benchmark "managed-bulk-25MB"
run_benchmark "managed-bulk-25MB"
run_benchmark "managed-bulk-25MB"
run_benchmark "managed-bulk-25MB"
run_benchmark "managed-bulk-25MB"
# 5 times for allocated buffers
run_benchmark "allocate-bulk-25MB"
run_benchmark "allocate-bulk-25MB"
run_benchmark "allocate-bulk-25MB"
run_benchmark "allocate-bulk-25MB"
run_benchmark "allocate-bulk-25MB"
# 5 times for direct buffers
run_benchmark "direct-bulk-25MB"
run_benchmark "direct-bulk-25MB"
run_benchmark "direct-bulk-25MB"
run_benchmark "direct-bulk-25MB"
run_benchmark "direct-bulk-25MB"

###############################################################################
# 50MB file
###############################################################################
# 5 times for managed buffers
run_benchmark "managed-bulk-50MB"
run_benchmark "managed-bulk-50MB"
run_benchmark "managed-bulk-50MB"
run_benchmark "managed-bulk-50MB"
run_benchmark "managed-bulk-50MB"
# 5 times for allocated buffers
run_benchmark "allocate-bulk-50MB"
run_benchmark "allocate-bulk-50MB"
run_benchmark "allocate-bulk-50MB"
run_benchmark "allocate-bulk-50MB"
run_benchmark "allocate-bulk-50MB"
# 5 times for direct buffers
run_benchmark "direct-bulk-50MB"
run_benchmark "direct-bulk-50MB"
run_benchmark "direct-bulk-50MB"
run_benchmark "direct-bulk-50MB"
run_benchmark "direct-bulk-50MB"

###############################################################################
# 100MB file
###############################################################################
# 5 times for managed buffers
run_benchmark "managed-bulk-100MB"
run_benchmark "managed-bulk-100MB"
run_benchmark "managed-bulk-100MB"
run_benchmark "managed-bulk-100MB"
run_benchmark "managed-bulk-100MB"
# 5 times for allocated buffers
run_benchmark "allocate-bulk-100MB"
run_benchmark "allocate-bulk-100MB"
run_benchmark "allocate-bulk-100MB"
run_benchmark "allocate-bulk-100MB"
run_benchmark "allocate-bulk-100MB"
# 5 times for direct buffers
run_benchmark "direct-bulk-100MB"
run_benchmark "direct-bulk-100MB"
run_benchmark "direct-bulk-100MB"
run_benchmark "direct-bulk-100MB"
run_benchmark "direct-bulk-100MB"

###############################################################################
# 200MB file
###############################################################################
# 5 times for managed buffers
run_benchmark "managed-bulk-200MB"
run_benchmark "managed-bulk-200MB"
run_benchmark "managed-bulk-200MB"
run_benchmark "managed-bulk-200MB"
run_benchmark "managed-bulk-200MB"
# 5 times for allocated buffers
run_benchmark "allocate-bulk-200MB"
run_benchmark "allocate-bulk-200MB"
run_benchmark "allocate-bulk-200MB"
run_benchmark "allocate-bulk-200MB"
run_benchmark "allocate-bulk-200MB"
# 5 times for direct buffers
run_benchmark "direct-bulk-200MB"
run_benchmark "direct-bulk-200MB"
run_benchmark "direct-bulk-200MB"
run_benchmark "direct-bulk-200MB"
run_benchmark "direct-bulk-200MB"
