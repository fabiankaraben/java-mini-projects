import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '30s', target: 500 }, // Ramp up to 500 users
    { duration: '1m', target: 2000 }, // Ramp up to 2000 users
    { duration: '30s', target: 0 },   // Ramp down to 0 users
  ],
};

export default function () {
  // Simulate a 100ms blocking operation on the server
  const res = http.get('http://localhost:8080/block?delayMs=100');
  
  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 200ms': (r) => r.timings.duration < 200,
  });
  
  sleep(1);
}
