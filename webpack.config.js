const path = require('path');

module.exports = {
  resolve: {
    fallback: {
      path: require.resolve('path-browserify'),
      fs: false,
      os: false,
      util: false,
      stream: false,
      crypto: false,
      buffer: false,
      assert: false,
    },
    alias: {
      app: path.resolve(__dirname, 'src/main/webapp/app/'),
    },
  },
  optimization: {
    removeAvailableModules: false,
    removeEmptyChunks: false,
    splitChunks: false,
  },
};
