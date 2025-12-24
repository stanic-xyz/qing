# EntityId MapStruct 转换器使用指南

## 概述

`AggregateMapper` 提供了 `EntityId<T>` 与基础类型之间的通用转换功能，支持在 MapStruct 中进行类型安全的转换。

## 核心功能

### 1. 基础类型提取方法

```java
@Named("getLongId")
public Long getLongId(EntityId<Long> id)

@Named("getStringId")
public String getStringId(EntityId<String> id)

@Named("getIntegerId")
public Integer getIntegerId(EntityId<Integer> id)

@Named("getValue")
public <T> T getValue(EntityId<T> id)
```

### 2. 通用EntityId创建方法

```java
@Named("createEntityId")
public <T, E extends EntityId<T>> E createEntityId(T value, Class<E> entityIdClass)
```

## 使用方式

### 方式一：使用预定义的类型提取方法（推荐）

```java
@Mapper(uses = {AggregateMapper.class})
public interface YourMapper {
    
    // 实体转DTO - 提取ID值
    @Mapping(source = "id", target = "id", qualifiedByName = "getStringId")
    YourDto toDto(YourEntity entity);
    
    // DTO转实体 - 创建EntityId
    @Mapping(source = "id", target = "id", qualifiedByName = "stringToEntityId")
    YourEntity toEntity(YourDto dto);
    
    // 自定义转换方法
    @Named("stringToEntityId")
    default YourEntityId stringToEntityId(String id) {
        return id != null ? YourEntityId.of(id) : null;
    }
}
```

### 方式二：使用通用创建方法

```java
@Mapper(uses = {AggregateMapper.class})
public interface YourMapper {
    
    @Mapping(source = "id", target = "id", qualifiedByName = "getStringId")
    YourDto toDto(YourEntity entity);
    
    @Mapping(source = "id", target = "id", qualifiedByName = "createYourEntityId")
    YourEntity toEntity(YourDto dto);
    
    @Named("createYourEntityId")
    default YourEntityId createYourEntityId(String id) {
        AggregateMapper mapper = new AggregateMapper();
        return mapper.createEntityId(id, YourEntityId.class);
    }
}
```

### 方式三：直接使用静态工厂方法（最简单）

```java
@Mapper
public interface YourMapper {
    
    // 实体转DTO
    @Mapping(source = "id.value", target = "id")
    YourDto toDto(YourEntity entity);
    
    // DTO转实体
    @Mapping(source = "id", target = "id", qualifiedByName = "stringToEntityId")
    YourEntity toEntity(YourDto dto);
    
    @Named("stringToEntityId")
    default YourEntityId stringToEntityId(String id) {
        return YourEntityId.of(id);
    }
}
```

## EntityId 实现要求

为了支持通用转换，EntityId 子类需要满足以下条件之一：

1. **提供静态 `of` 方法**（推荐）：

```java
public static YourEntityId of(String value) {
    return new YourEntityId(value);
}
```

2. **提供公共构造函数**：

```java
public YourEntityId(String value) {
    this.value = value;
}
```

3. **重写 `ofValue` 方法**：

```java
@Override
public YourEntityId ofValue(String value) {
    return new YourEntityId(value);
}
```

## 最佳实践

1. **优先使用静态工厂方法**：为每个 EntityId 子类提供 `of(T value)` 静态方法
2. **使用类型安全的转换**：使用预定义的类型提取方法而不是通用方法
3. **保持一致性**：在同一个项目中使用统一的转换方式
4. **性能考虑**：静态工厂方法比反射创建性能更好

## 示例项目结构

```
src/main/java/
├── domain/
│   ├── YourEntity.java
│   └── YourEntityId.java
├── dto/
│   └── YourDto.java
└── converter/
    ├── AggregateMapper.java
    └── YourMapper.java
```

## 注意事项

1. 确保 EntityId 子类有合适的创建方法
2. 在使用反射创建时，注意异常处理
3. 考虑使用缓存来提高反射性能
4. 遵循 MapStruct 的最佳实践
