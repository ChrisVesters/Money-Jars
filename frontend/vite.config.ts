import { defineConfig } from 'vite'
import babel from '@rolldown/plugin-babel'
import react, { reactCompilerPreset } from '@vitejs/plugin-react'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    babel({ presets: [reactCompilerPreset()] }),
  ],
  resolve: {
    alias: {
      '@assets': path.resolve(import.meta.dirname, './src/assets'),
      '@gql': path.resolve(import.meta.dirname, './src/gql'),
	  '@src': path.resolve(import.meta.dirname, './src'),
    },
  },
})
