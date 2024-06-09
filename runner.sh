#!/bin/bash

mkdir "$HOME/Downloads/iso/data/"

# Function to perform the curl request
perform_curl() {
    local url="$1"
    local output_file="$2"
    curl --location "$url" --output "$HOME/Downloads/iso/data/$output_file"
}

# Number of times to run the request
num_requests=1

# Base URL for the request
base_url='http://localhost:8080/api/file-server/v1/file/download?filePath=%2Fiso%2Fubuntu-v2.iso'

# Array to hold background process IDs
pids=()

# Loop to run the requests in parallel
for ((i=1; i<=$num_requests; i++)); do
    output_file="ubuntu-$(date +%Y%m%d%H%M%S)-$i.iso"
    perform_curl "$base_url" "$output_file" &
    pids+=($!)
done

# Wait for all background processes to finish
for pid in "${pids[@]}"; do
    wait "$pid"
done

echo "All requests completed."

