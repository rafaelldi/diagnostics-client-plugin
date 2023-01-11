<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# diagnostics-client-plugin Changelog

## [Unreleased]
### Added
- Action to monitor traces
- Auto refresh for process tree

### Changed
- Update dependencies
- Process dashboard UI improvements

## [2022.3.0]
### Added
- Action to collect GC events
- Persist dialog field values
- Compatibility with Rider 2022.3

## [1.2.0]
### Added
- Action to monitor GC events
- Action to collect stack traces

## [1.1.0]
### Added
- Support for 2022.2 version of the platform
- Additional information about the process
- Actions to monitor and collect System.Diagnostics.Metrics
- Help actions

### Changed
- Default name for trace and dump files

### Fixed
- Trace provider parsing algorithm

## [1.0.0]
### Added
- Initial scaffold created from [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
- Action to collect dump
- Actions to monitor and collect counters
- Action to collect traces