if ('service-worker' in navigator) {
  navigator.serviceWorker.register('/service-worker.js')
    .then(reg => console.log('Service worker registered: ', reg))
    .catch(err => console.error('Cannot register server worker: ', err));
}