# System

```text
eventstorming/Element/System
```

```text
include('eventstorming/Element/System')
```

|                                  System                                  |
|:------------------------------------------------------------------------:|
| ![illustration for System](../../eventstorming/Element/System.Local.png) |

## System

### Load remotely

```plantuml
@startuml
' configures the library
!global $LIB_BASE_LOCATION="https://raw.githubusercontent.com/tmorin/plantuml-libs/master/distribution"

' loads the library's bootstrap
!include $LIB_BASE_LOCATION/bootstrap.puml

' loads the package bootstrap
include('eventstorming/bootstrap')

' loads the Item which embeds the element System
include('eventstorming/Element/System')

System('System') [
System
--
A third-party service provider such as a payment gateway or shipping company.
]
@enduml
```

### Load locally

```plantuml
@startuml
' configures the library
!global $INCLUSION_MODE="local"
!global $LIB_BASE_LOCATION="../.."

' loads the library's bootstrap
!include $LIB_BASE_LOCATION/bootstrap.puml

' loads the package bootstrap
include('eventstorming/bootstrap')

' loads the Item which embeds the element System
include('eventstorming/Element/System')

System('System') [
System
--
A third-party service provider such as a payment gateway or shipping company.
]
@enduml
```

