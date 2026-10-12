import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // Send API calls to the Spring Boot backend, so we don't need CORS
    proxy: {
      '/houses': 'http://localhost:8080',
    },
  },
})
