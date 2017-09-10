# storage-notifications
Service to provide asynchronous notifications to clients about storage updates.

## Installation
```
$ git clone https://github.com/houthacker/storage-notifications.git
```

## Running
### Service
For development versions, checkout the ```develop``` branch. For releases,
use the ```tags/<version>``` refs.
```
$ cd storage-notifications/
$ git checkout <refspec>
$ mvn clean package
$ cp config.yml.example config.yml
$ java -jar target/storage-notifications-<version>.jar server config.yml
```

### Clients
Clients use the REST service to announce themselves, and to configure what updates they get.