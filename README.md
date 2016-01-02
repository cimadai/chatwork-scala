chatwork-scala
==============

[![Circle CI](https://circleci.com/gh/cimadai/chatwork-scala.svg?style=svg)](https://circleci.com/gh/cimadai/chatwork-scala)

A chatwork client for scala.

## Usage

1.  build.sbt

```sh
libraryDependencies += "net.cimadai" %% "chatwork-scala" % "1.0.1"
```

2. Set environment variables.
```sh
val apiKey = "YOUR API KEY FOR CHATWORK"
val cli = new ChatworkClient(apiKey)
cli.me()
cli.postRoomMessage("SOME CHATROOM ID", "A MESSAGE")
```

## License
The MIT License. See `LICENSE` file.
