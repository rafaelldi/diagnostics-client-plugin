<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# diagnostics-client-plugin Changelog

## [Unreleased]

### Added
- Go to settings action
- Charts for exception and thread count

### Changed
- Support for Rider 2023.3

## [2023.2.1] - 2023-08-15

### Added
- GC and exception markers to the chart
- Automatically attach charts to a starting process 
- Some actions to the chart panel

## [2023.2.0] - 2023-08-07

### Changed
- Extracted [Diagnostics Agent](https://github.com/rafaelldi/diagnostics-agent) to a separate tool
- Created a [plugin documentation portal](https://rafaelldi.blog/diagnostics-client-plugin)
- Support for Rider 2023.2
- Improve counters dialog

## [2023.1.2] - 2023-05-03

### Fixed
- Process explorer issue

### Changed
- Chart improvements

## [2023.1.1] - 2023-04-06

### Added
- Action to monitor charts
- Recent Artifacts tab
- Attach debugger action

### Changed
- Support for Rider 2023.1

## [2023.1.0] - 2023-01-19

### Added
- Action to monitor traces
- Auto refresh for process tree
- Environment variables to the process dashboard
- Compatibility with Rider 2023.1

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

[Unreleased]: https://github.com/rafaelldi/diagnostics-client-plugin/compare/v2023.2.1...HEAD
[2023.2.1]: https://github.com/rafaelldi/diagnostics-client-plugin/compare/v2023.2.0...v2023.2.1
[2023.2.0]: https://github.com/rafaelldi/diagnostics-client-plugin/compare/v2023.1.2...v2023.2.0
[2023.1.2]: https://github.com/rafaelldi/diagnostics-client-plugin/compare/v2023.1.1...v2023.1.2
[2023.1.1]: https://github.com/rafaelldi/diagnostics-client-plugin/compare/v2023.1.0...v2023.1.1
[2023.1.0]: https://github.com/rafaelldi/diagnostics-client-plugin/compare/v2022.3.0...v2023.1.0
[2022.3.0]: https://github.com/rafaelldi/diagnostics-client-plugin/compare/v1.2.0...v2022.3.0
[1.2.0]: https://github.com/rafaelldi/diagnostics-client-plugin/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/rafaelldi/diagnostics-client-plugin/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/rafaelldi/diagnostics-client-plugin/commits/v1.0.0
