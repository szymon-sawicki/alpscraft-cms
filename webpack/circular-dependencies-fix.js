class CircularDependencyWorkaroundPlugin {
  apply(compiler) {
    compiler.hooks.compilation.tap('CircularDependencyWorkaroundPlugin', compilation => {
      // Override the method that's causing the error
      const originalMethod = compilation.moduleGraph.getParentModule;
      compilation.moduleGraph.getParentModule = function (dependency) {
        try {
          const result = originalMethod.call(this, dependency);
          return result;
        } catch (e) {
          // Return a dummy object with buildMeta property to avoid errors
          return { buildMeta: {} };
        }
      };
    });
  }
}

module.exports = CircularDependencyWorkaroundPlugin;
