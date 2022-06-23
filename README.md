# Solana4j

---
Solana4j is a Java library aimed at interacting with the Solana RPC network providing REST and Websockets clients.

Additional documentation such as the [RPC API Docs](https://docs.solana.com/developing/clients/jsonrpc-api), as well as the community maintained [Solana Cookbook](https://solanacookbook.com/#contributing) may be helpful.

## Requirements
 - Java 16+

## Compiling and Using
To compile the library use the command below,
```
./gradlew build
```

If you are nto interested in contributing and just utilizing the library, add the following dependencies increasing the version or commit hash as required.
### Gradle
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
	dependencies {
	        implementation 'com.github.Chasewhip8:Solana4j:5a596b58f5'
	}
```

### Maven
```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
	
	<dependency>
	    <groupId>com.github.Chasewhip8</groupId>
	    <artifactId>Solana4j</artifactId>
	    <version>5a596b58f5</version>
	</dependency>
```

## Contributing
Make a PR with an accurate description disclosing the changes made, all code falls under the project license.

## License
Solana4j is available under the MIT license. See the LICENSE file for more info.

## Credits
Shutout to Solanaj for some initial inspiration and paving the way, if your interested in checking them out you can find them [here](https://github.com/p2p-org/solanaj).
