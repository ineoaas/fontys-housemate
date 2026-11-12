// Runs once before all tests: fail fast with a clear message if the backend isn't running,
// instead of every test timing out on API calls.
const BACKEND_URL = 'http://localhost:8080'

export default async function globalSetup() {
  try {
    // Any HTTP response (even 404) means the server is up
    await fetch(BACKEND_URL)
  } catch {
    throw new Error(
      `Backend is not reachable at ${BACKEND_URL}.\n` +
        'Start it from the Backend folder with `docker compose up -d --build`\n' +
        '(or `docker compose up -d database` and run the app from IntelliJ).',
    )
  }
}
