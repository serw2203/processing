# processing

#OpenShift
```
https://habr.com/ru/company/redhatrussia/blog/511954/
https://habr.com/ru/company/redhatrussia/blog/513948/
https://habr.com/ru/company/redhatrussia/blog/515988/

tekton
https://docs.openshift.com/container-platform/4.5/pipelines/creating-applications-with-cicd-pipelines.html
```
#OpenSSL
```
https://habr.com/ru/post/417069/  ???
https://sysadmins.ru/topic238106.html
https://www.nixp.ru/articles/12.html
http://xgu.ru/wiki/OpenSSL
```

oc start-build processing --from-dir . --follow


Tutorial: Building private code
Create a local directory to hold your code:

https://docs.openshift.com/container-platform/3.6/dev_guide/dev_tutorials/binary_builds.html#binary-builds-private-code


```
mkdir myapp
cd myapp
```
In the directory create a file named Dockerfile with the following content:

```dockerfile
FROM centos:centos7

EXPOSE 8080

COPY index.html /var/run/web/index.html

CMD cd /var/run/web && python -m SimpleHTTPServer 8080`
```

Create a file named index.html with the following content:

```html
<html>
  <head>
    <title>My local app</title>
  </head>
  <body>
    <h1>Hello World</h1>
    <p>This is my local application</p>
  </body>
</html>
```
Create a new build for your application:

```cmd
oc new-build --strategy docker --binary --docker-image centos:centos7 --name myapp
```
Start a binary build using the local directory’s content:

```
oc start-build myapp --from-dir . --follow
```
Deploy the application using new-app, then create a route for it:

```
oc new-app myapp
oc expose svc/myapp
```
Get the host name for your route and navigate to it:

```
oc get route myapp
```
PATH=~/Desktop/processing/ui/node:$PATH

html-webpack-plugin
fork-ts-checker-webpack-plugin

new HtmlWebpackPlugin ({ 
    template: path.resolve(__dirname, './static/index.html'),
    filename: 'index.html'
});

new ForkTsCheckerWebPackPlugin ({
typescript: {
    memoryLimit: 4096,
    tsconfig: path.resolve(__dirname, 'tsconfig.json')
}
})


curl -d '{ "test" : { "id": "value1", "name": "valu2" } }' -H "Content-Type: application/json" -X POST http://localhost:8080/test
