#!/bin/bash

# Start the application in the background
echo "Starting application..."
# Always build to ensure latest code
echo "Building project..."
mvn clean package -DskipTests

# Cleanup any existing process on port 8080
echo "Cleaning up port 8080..."
lsof -ti:8080 | xargs kill -9 2>/dev/null || true
sleep 2

# Run directly, without redirection, so output is captured by the calling tool
java -jar target/signal-handling-shutdown-1.0-SNAPSHOT.jar &
PID=$!

# Wait for the server to start (check port availability)
echo "Waiting for server to initialize..."
for i in {1..10}; do
    if lsof -i:8080 > /dev/null; then
        echo "Server is up!"
        break
    fi
    echo "Waiting..."
    sleep 1
done

# Send a request to the long running process in the background
echo "Sending request to /long-process..."
curl -v http://localhost:8080/long-process &

# Give it a moment to receive the request
sleep 1

# Send SIGTERM (kill -15) to the java process
# Note: In non-interactive background scripts, SIGINT (kill -2) is often ignored.
# SIGTERM reliably triggers the same Shutdown Hook logic as SIGINT (Ctrl+C).
echo "Sending SIGTERM to process $PID..."
kill -15 $PID

# Wait for the process to finish with a timeout
echo "Waiting for process to exit..."
count=0
while kill -0 $PID 2>/dev/null; do
    sleep 1
    count=$((count+1))
    if [ $count -ge 15 ]; then
        echo "Timeout waiting for process to exit!"
        kill -9 $PID
        exit 1
    fi
done

echo "Test finished. Please verify the logs above:"
echo "1. '[Shutdown Hook] Shutdown signal received...' should appear."
echo "2. '[Shutdown Hook] Draining active requests...' should appear."
echo "3. '[Handler] Long process finished...' should appear BEFORE '[Shutdown Hook] Server stopped gracefully'."
