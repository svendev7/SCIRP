/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{ts,tsx}'],
  theme: {
    extend: {
      colors: {
        brand: {
          50: '#eef9ff',
          100: '#c6ecff',
          200: '#8dd7ff',
          300: '#55c3ff',
          400: '#1daeff',
          500: '#0095e6',
          600: '#0073b4',
          700: '#005282',
          800: '#003150',
          900: '#001f33',
        },
        status: {
          open: '#f97316',
          inProgress: '#facc15',
          resolved: '#22c55e',
          closed: '#9ca3af',
        },
      },
    },
  },
  plugins: [],
};

