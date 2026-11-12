import { defineConfig, devices } from '@playwright/test'

// End-to-end tests for the whole stack: browser -> Vite (frontend) -> Spring Boot (backend) -> Postgres.
//
// Before running, start the backend (from the Backend folder):
//   docker compose up -d --build      or   docker compose up -d database   + run the app from IntelliJ
// Vite is started automatically below (or reused if `npm run dev` is already running).
export default defineConfig({
  testDir: './tests',
  globalSetup: './global-setup.js',
  use: {
    baseURL: 'http://localhost:5173',
    trace: 'on-first-retry',
  },
  projects: [{ name: 'chromium', use: { ...devices['Desktop Chrome'] } }],
  webServer: {
    command: 'npm run dev',
    cwd: '../Frontend',
    url: 'http://localhost:5173',
    reuseExistingServer: true,
  },
})
