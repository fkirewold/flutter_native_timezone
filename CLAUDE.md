# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

flutter_timezone is a Flutter plugin that provides timezone information from native platform APIs. It supports Android, iOS, Linux, macOS, Windows, and Web platforms, offering two main methods:
- `getLocalTimezone()`: Returns the device's current timezone identifier
- `getAvailableTimezones()`: Returns a list of all available timezone identifiers

## Development Commands

### Testing
- **Run unit tests**: `flutter test`
- **Run integration tests**: `flutter test integration_test/` (from example directory)
- **Run single test file**: `flutter test test/flutter_timezone_test.dart`
- **Windows integration tests**: Use `run_integration_test_windows.ps1` script in example directory

### Code Quality
- **Lint code**: `flutter analyze` 
- **Format code**: `dart format .`

### Build and Development
- **Get dependencies**: `flutter pub get`
- **Run example app**: `cd example && flutter run`
- **Build for specific platform**: `cd example && flutter build <platform>`

## Architecture

### Core Structure
- **lib/flutter_timezone.dart**: Main Dart API using MethodChannel communication
- **lib/flutter_timezone_web.dart**: Web platform implementation
- **Platform-specific implementations**: Each platform directory contains native code that responds to MethodChannel calls

### Platform Implementations
- **Android**: Kotlin implementation in `android/src/main/kotlin/net/wolverinebeach/flutter_timezone/FlutterTimezonePlugin.kt`
- **iOS**: Objective-C implementation in `ios/flutter_timezone/Sources/flutter_timezone/FlutterTimezonePlugin.m`  
- **macOS**: Swift implementation in `macos/flutter_timezone/Sources/flutter_timezone/FlutterTimezonePlugin.swift`
- **Linux**: C++ implementation in `linux/flutter_timezone_plugin.cc`
- **Windows**: C++ implementation in `windows/flutter_timezone_plugin.cpp`
- **Web**: Dart implementation using browser APIs

### Communication Pattern
All platforms implement the same MethodChannel interface (`flutter_timezone`) with two methods:
- `getLocalTimezone`: Returns timezone identifier string
- `getAvailableTimezones`: Returns list of timezone identifier strings

### Testing Architecture
- **Unit tests**: Mock MethodChannel calls in `test/flutter_timezone_test.dart`
- **Integration tests**: Full end-to-end testing in `example/integration_test/local_timezone_test.dart`
- **Platform tests**: Native unit tests in `windows/test/` and `linux/test/` directories

## Code Style Notes
- Uses comprehensive linting rules from `analysis_options.yaml` based on `package:flutter_lints/flutter.yaml`
- Prefers single quotes for strings
- Does not require trailing commas or public API documentation
- Excludes generated files and build directories from analysis