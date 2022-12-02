# Prerequisites
* [JDK 17](https://github.com/JetBrains/JetBrainsRuntime/tree/jbr17#releases)
* [.NET 7](https://dotnet.microsoft.com/en-us/download/dotnet/7.0)

# Development
* To develop Kotlin part, open `diagnostics-client-plugin` folder.
* To develop C# part, open `diagnostics-client-plugin.sln` file in the main folder.
* To connect them, there is a `protocol` subfolder. To read more about this protocol, see [this library](https://github.com/JetBrains/rd).

# Run/Debug
* Run/Debug plugin with `Run Plugin` run configuration (from Kotlin part) and `Rider` run configuration (from C# part).
* Generate protocol models with `Generate Protocol` run configuration (from Kotlin part) and `Generate protocol` run configuration (from C# part).

# Documentation
* [IntelliJ Platform SDK](https://plugins.jetbrains.com/docs/intellij/welcome.html#getting-started)
* [ReSharper Platform SDK](https://www.jetbrains.com/help/resharper/sdk/welcome.html)