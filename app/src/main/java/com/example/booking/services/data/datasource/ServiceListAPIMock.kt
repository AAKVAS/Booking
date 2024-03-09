package com.example.booking.services.data.datasource

import com.example.booking.common.utils.getUUID
import com.example.booking.services.domain.model.Service

// TODO: Удалить, когда ретрофитку подключу
class ServiceListAPIMock : ServiceListAPI {
    private var lastId: Long = 1

    private var titles: Set<String> = setOf(
        "Название",
        "Собака на пене",
        "Янки",
        "Ленком"
    )

    private var descriptions: Set<String> = setOf(
        "Описание 1",
        "Большое и замечательное описание, чтобы проверить, как будет выглядеть большое количество текста на интерфейсе",
        "",
        "Просто текст"
    )

    override suspend fun fetchServices(
        userLogin: String,
        cityId: Long,
        searchPattern: String,
        page: Int,
        size: Int,
    ): List<Service> {
        return (1..size).map {
            randomService()
        }.filter {
            it.title.contains(searchPattern, true)
                || it.address.contains(searchPattern)
        }
    }

    override suspend fun getServiceDetails(userLogin: String, serviceId: Long): Service {
        return randomService()
    }

    private fun randomService(): Service {
        return Service(
            uid = getUUID(),
            id = lastId++,
            title = titles.random(),
            description = descriptions.random(),
            address = "Адрес",
            favorite = false,
            halls = listOf(),
            imageLink = "https://bizweb.dktcdn.net/100/099/559/files/thiet-ke-quan-cafe-dep-7.jpg?v=1479583152061"
        )
    }
}