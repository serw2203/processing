//https://vanilla-js.blogspot.com/2019/07/nodejs-https-tlsssl.html
//https://issue.life/questions/49553138

//https://blog.regolit.com/2016/01/13/digital-certs-for-humans

//rm -f *.pem *.srl && ./make.sh
//rm -f *.pem *.srl && openssl req -new -x509 -days 9999 -config ca.cnf -keyout ca-key.pem -out ca-crt.pem

const https = require('https');
const fs = require('fs');

const server_options = {
  key: fs.readFileSync('server-key.pem'),
  cert: fs.readFileSync('server-crt.pem'),
};

https.createServer(server_options, (request, response) => {
  console.info(`\n${new Date().toLocaleString()} ${request.headers.host} ${request.url}`);
  response.writeHead(200);
  response.end("Hello world!");
}).listen(443);

const client_options = {
  method: 'GET',
  hostname: '192.168.44.1.xip.io',
  port: 443,
  path: '/',
  // key: fs.readFileSync('client1-key.pem'),
  // cert: fs.readFileSync('client1-crt.pem'),
  ca: fs.readFileSync('ca-crt.pem')
};

global.setInterval(() => https.request(client_options,
  (response) => response.on('data', (data) => process.stdout.write(data))).on('error', (e) => console.error(e)).end(), 1000);

global.setInterval(() => https.request({ ...client_options, hostname: '192.168.55.1.xip.io' },
  (response) => response.on('data', (data) => process.stdout.write(data))).on('error', (e) => console.error(e)).end(), 5000);

// const server_options8 = {
//   key: fs.readFileSync('ca-key.pem'),
//   cert: fs.readFileSync('ca-crt.pem'),
//   passphrase: 'password'
// };

// https.createServer(server_options8, (request, response) => {
//   console.info(`\n${new Date().toLocaleString()} ${request.headers.host} ${request.url}`);
//   response.writeHead(200);
//   response.end("Hello world!");
// }).listen(8443);

// global.setInterval(() => https.request({ ...client_options, port: 8443 },
//   (response) => response.on('data', (data) => process.stdout.write(data))).on('error', (e) => console.error(e)).end(), 1000);

// global.setInterval(() => https.request({ ...client_options, hostname: '192.168.55.1.xip.io', port: 8443 },
//   (response) => response.on('data', (data) => process.stdout.write(data))).on('error', (e) => console.error(e)).end(), 5000);
