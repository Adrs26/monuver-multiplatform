package com.adrian.monuver.core.domain.util

object AccountType {
    const val CASH = 1
    const val BANK = 2
    const val E_WALLET = 3
    const val CREDIT_CARD = 4
    const val INVESTMENT = 5
}

object SelectAccountType {
    const val SOURCE = 1
    const val DESTINATION = 2
}

object TransactionType {
    const val ALL = 0
    const val INCOME = 1001
    const val EXPENSE = 1002
    const val TRANSFER = 1003
}

object TransactionParentCategory {
    const val SALARY = 1
    const val BONUSES = 2
    const val COMMISSION = 3
    const val INVESTMENT_RESULT = 4
    const val OTHER_INCOME = 5
    const val FOOD_BEVERAGES = 6
    const val BILLS_UTILITIES = 7
    const val TRANSPORTATION = 8
    const val HEALTH_PERSONAL_CARE = 9
    const val EDUCATION = 10
    const val SHOPPING = 11
    const val ENTERTAINMENT = 12
    const val OTHER_EXPENSE = 13
    const val TRANSFER = 14
    const val PARENT_INCOME = 111
}

object TransactionChildCategory {
    const val SALARY = 1
    const val BONUSES = 2
    const val COMMISSION = 3
    const val INVESTMENT_RESULT = 4
    const val OTHER_INCOME = 5
    const val FOOD = 61
    const val DRINK = 62
    const val TEA_COFFEE = 63
    const val GROCERY = 64
    const val WATER = 71
    const val ELECTRICITY = 72
    const val GAS = 73
    const val TAX = 74
    const val HOUSE = 75
    const val INTERNET = 76
    const val MAINTENANCE = 81
    const val FUEL = 82
    const val VEHICLE_ACCESSORIES = 83
    const val ONLINE_RIDE = 84
    const val PUBLIC_TRANSPORT = 85
    const val HOSPITAL = 91
    const val DOCTOR = 92
    const val MEDICINE = 93
    const val PERSONAL_CARE = 94
    const val MASSAGE = 95
    const val SPA = 96
    const val GYM = 97
    const val LAUNDRY = 98
    const val EDUCATION_FEE = 101
    const val BOOKS_STATIONERY = 102
    const val COURSE = 103
    const val PRINT_COPY = 104
    const val CLOTHING = 111
    const val SHOES = 112
    const val BAG = 113
    const val ACCESSORIES = 114
    const val ELECTRONICS = 115
    const val FURNITURE = 116
    const val VEHICLE = 117
    const val DIGITAL_SUBSCRIPTION = 121
    const val CINEMA = 122
    const val GAMES = 123
    const val CONCERT_FESTIVAL = 124
    const val BOOKS_COMICS = 125
    const val HOBBIES_COLLECTIONS = 126
    const val COMMUNITY = 127
    const val DONATION = 131
    const val INSURANCE = 132
    const val INVESTMENT = 133
    const val OTHER_EXPENSE = 134
    const val SAVINGS_COMPLETE = 135
    const val TRANSFER_ACCOUNT = 1003
    const val SAVINGS_IN = 1004
    const val SAVINGS_OUT = 1005
}

object Month {
    const val ALL = 0
    const val JANUARY = 1
    const val FEBRUARY = 2
    const val MARCH = 3
    const val APRIL = 4
    const val MAY = 5
    const val JUNE = 6
    const val JULY = 7
    const val AUGUST = 8
    const val SEPTEMBER = 9
    const val OCTOBER = 10
    const val NOVEMBER = 11
    const val DECEMBER = 12
}

object Cycle {
    const val YEARLY = 1
    const val MONTHLY = 2
    const val WEEKLY = 3
    const val CUSTOM = 4
}

object BudgetWarningCondition {
    const val LOW_REMAINING_BUDGET = 1
    const val FULL_BUDGET = 2
    const val OVER_BUDGET = 3
}