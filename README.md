# XY-Fleet
![Static Badge](https://img.shields.io/badge/vue-^3.3.7-green?logo=vuedotjs)
![Static Badge](https://img.shields.io/badge/bootstrap-%5E4.3.2-8A2BE2?logo=bootstrap)
![Static Badge](https://img.shields.io/badge/typescript-~5.2.0-yellow?logo=typescript)
![Static Badge](https://img.shields.io/badge/mariadb-^1.1.4-red?logo=mariadb)
*MATSE – SWE Project, WS 2023/24*

An basic tool for managing the car fleet.

## Getting Started

Using the shell:
```bash
git clone https://github.com/Gold-Squadron/XY-Fleet.git
```
or use File -> New -> Project from VC [https://github.com/Gold-Squadron/XY-Fleet.git]

You can now either open /frontend and /backend as seperate projects or add them both as Modules to your Workspace.

> [!TIP]
> Project Files for Intellij and Webstorm are already in place.

### Frontend (Vue)

```sh
#GOTO project root folder
cd frontend
npm install
npm run dev      # for testing
npm run build    # for building
```


#### IDE Setup:
Install the Vue.js Plugin from Jetbrains
https://plugins.jetbrains.com/plugin/9442-vue-js

### Database (MariaDB)
````shell
#GOTO project root folder
cd sql
docker compose -f compose.yml up mariadb -d
````
If you have mariadb properly installed, you can call it in the console like this.
```shell
mariadb --defaults-file ./connection_parameters.cnf init.sql
```
Otherwise, you can execute the init.sql script manually form your Database IDE on the docker image.
### Backend (Java)
if you have Maven properly installed, you can call it in the console like this.
````shell
#GOTO project root folder
cd backend
mvn clean install
mvn jooq-codegen:generate
````
Or you need to simply build and execute the backend module with Maven inside your IDE.
If there still occure some problems, you should manually execute the jooq-codegen:generate plugin-commmand.
