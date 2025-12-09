package com.adrian.monuver.core.domain.util

object DataProvider {
    fun provideAccountTypes(): List<Int> {
        return listOf(
            AccountType.CASH,
            AccountType.BANK,
            AccountType.E_WALLET,
            AccountType.CREDIT_CARD,
            AccountType.INVESTMENT
        )
    }

    fun provideIncomeCategories(): Map<Int, List<Int>> {
        return mapOf(
            TransactionParentCategory.PARENT_INCOME to listOf(
                TransactionChildCategory.SALARY,
                TransactionChildCategory.BONUSES,
                TransactionChildCategory.COMMISSION,
                TransactionChildCategory.INVESTMENT_RESULT,
                TransactionChildCategory.OTHER_INCOME
            )
        )
    }

    fun provideExpenseCategories(): Map<Int, List<Int>> {
        return mapOf(
            TransactionParentCategory.FOOD_BEVERAGES to listOf(
                TransactionChildCategory.FOOD,
                TransactionChildCategory.DRINK,
                TransactionChildCategory.TEA_COFFEE,
                TransactionChildCategory.GROCERY
            ),
            TransactionParentCategory.BILLS_UTILITIES to listOf(
                TransactionChildCategory.WATER,
                TransactionChildCategory.ELECTRICITY,
                TransactionChildCategory.GAS,
                TransactionChildCategory.TAX,
                TransactionChildCategory.HOUSE,
                TransactionChildCategory.INTERNET
            ),
            TransactionParentCategory.TRANSPORTATION to listOf(
                TransactionChildCategory.MAINTENANCE,
                TransactionChildCategory.FUEL,
                TransactionChildCategory.VEHICLE_ACCESSORIES,
                TransactionChildCategory.ONLINE_RIDE,
                TransactionChildCategory.PUBLIC_TRANSPORT
            ),
            TransactionParentCategory.HEALTH_PERSONAL_CARE to listOf(
                TransactionChildCategory.HOSPITAL,
                TransactionChildCategory.DOCTOR,
                TransactionChildCategory.MEDICINE,
                TransactionChildCategory.PERSONAL_CARE,
                TransactionChildCategory.MASSAGE,
                TransactionChildCategory.SPA,
                TransactionChildCategory.GYM,
                TransactionChildCategory.LAUNDRY
            ),
            TransactionParentCategory.EDUCATION to listOf(
                TransactionChildCategory.EDUCATION_FEE,
                TransactionChildCategory.BOOKS_STATIONERY,
                TransactionChildCategory.COURSE,
                TransactionChildCategory.PRINT_COPY
            ),
            TransactionParentCategory.SHOPPING to listOf(
                TransactionChildCategory.CLOTHING,
                TransactionChildCategory.SHOES,
                TransactionChildCategory.BAG,
                TransactionChildCategory.ACCESSORIES,
                TransactionChildCategory.ELECTRONICS,
                TransactionChildCategory.FURNITURE,
                TransactionChildCategory.VEHICLE
            ),
            TransactionParentCategory.ENTERTAINMENT to listOf(
                TransactionChildCategory.DIGITAL_SUBSCRIPTION,
                TransactionChildCategory.CINEMA,
                TransactionChildCategory.GAMES,
                TransactionChildCategory.CONCERT_FESTIVAL,
                TransactionChildCategory.BOOKS_COMICS,
                TransactionChildCategory.HOBBIES_COLLECTIONS,
                TransactionChildCategory.COMMUNITY
            ),
            TransactionParentCategory.OTHER_EXPENSE to listOf(
                TransactionChildCategory.DONATION,
                TransactionChildCategory.INSURANCE,
                TransactionChildCategory.INVESTMENT,
                TransactionChildCategory.OTHER_EXPENSE
            ),
        )
    }

    fun provideExpenseParentCategories(): List<Int> {
        return listOf(
            TransactionParentCategory.FOOD_BEVERAGES,
            TransactionParentCategory.BILLS_UTILITIES,
            TransactionParentCategory.TRANSPORTATION,
            TransactionParentCategory.HEALTH_PERSONAL_CARE,
            TransactionParentCategory.EDUCATION,
            TransactionParentCategory.SHOPPING,
            TransactionParentCategory.ENTERTAINMENT,
            TransactionParentCategory.OTHER_EXPENSE
        )
    }

    fun provideTransactionTypeFilterOptions(): List<Int> {
        return listOf(
            TransactionType.ALL,
            TransactionType.INCOME,
            TransactionType.EXPENSE,
            TransactionType.TRANSFER,
        )
    }

    fun provideMonthFilterOptions(): List<Int> {
        return listOf(
            Month.ALL,
            Month.JANUARY,
            Month.FEBRUARY,
            Month.MARCH,
            Month.APRIL,
            Month.MAY,
            Month.JUNE,
            Month.JULY,
            Month.AUGUST,
            Month.SEPTEMBER,
            Month.OCTOBER,
            Month.NOVEMBER,
            Month.DECEMBER
        )
    }
}