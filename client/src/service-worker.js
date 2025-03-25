importScripts('https://storage.googleapis.com/workbox-cdn/releases/7.3.0/workbox-sw.js');

const routing = workbox.routing;
const strategies = workbox.strategies;
const expiration = workbox.expiration;

// Cache assets
routing.registerRoute(
	/\.(?:css|js|jsx|ts|json|woff|woff2|ttf|otf)$/,
	new strategies.StaleWhileRevalidate({
		"cacheName": "assets",
		plugins: [
			new expiration.Plugin({
				maxEntries: 1000,
				maxAgeSeconds: 15552000	// approx 6mths
			})
		]
	})
);

// Cache images 
routing.registerRoute(
	/.(?:png|jpg|jpeg|gif|svg|webp)$/,
	new strategies.CacheFirst({
		"cacheName": "images",
		plugins: [
			new expiration.Plugin({
				maxEntries: 1000,
				maxAgeSeconds: 15552000	// approx 6mths
			})
		]
	})
);

// Cache HTML pages
routing.registerRoute(
	/.*\.html$/,
	new strategies.NetworkFirst({
		"cacheName": "pages",
		plugins: [
			new expiration.Plugin({
				maxEntries: 50,
				maxAgeSeconds: 604800 	// 7d
			})
		]
	})
);

