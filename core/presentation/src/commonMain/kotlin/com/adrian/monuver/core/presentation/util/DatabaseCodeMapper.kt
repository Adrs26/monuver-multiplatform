package com.adrian.monuver.core.presentation.util

import androidx.compose.ui.graphics.Color
import com.adrian.monuver.core.domain.util.AccountType
import com.adrian.monuver.core.domain.util.Cycle
import com.adrian.monuver.core.domain.util.Month
import com.adrian.monuver.core.domain.util.TransactionChildCategory
import com.adrian.monuver.core.domain.util.TransactionParentCategory
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.core.presentation.theme.Amber500
import com.adrian.monuver.core.presentation.theme.Blue400
import com.adrian.monuver.core.presentation.theme.Blue800
import com.adrian.monuver.core.presentation.theme.BlueGrey400
import com.adrian.monuver.core.presentation.theme.Brown400
import com.adrian.monuver.core.presentation.theme.DeepPurple400
import com.adrian.monuver.core.presentation.theme.Green400
import com.adrian.monuver.core.presentation.theme.Green600
import com.adrian.monuver.core.presentation.theme.Green800
import com.adrian.monuver.core.presentation.theme.Indigo400
import com.adrian.monuver.core.presentation.theme.Orange600
import com.adrian.monuver.core.presentation.theme.Pink400
import com.adrian.monuver.core.presentation.theme.Teal400
import com.adrian.monuver.core.presentation.theme.Teal600
import monuver.core.presentation.generated.resources.Res
import monuver.core.presentation.generated.resources.accessories
import monuver.core.presentation.generated.resources.all
import monuver.core.presentation.generated.resources.apr
import monuver.core.presentation.generated.resources.april
import monuver.core.presentation.generated.resources.aug
import monuver.core.presentation.generated.resources.august
import monuver.core.presentation.generated.resources.bag
import monuver.core.presentation.generated.resources.bank
import monuver.core.presentation.generated.resources.bills_utilities
import monuver.core.presentation.generated.resources.bonuses
import monuver.core.presentation.generated.resources.books_comics
import monuver.core.presentation.generated.resources.books_stationery
import monuver.core.presentation.generated.resources.cash
import monuver.core.presentation.generated.resources.cinema
import monuver.core.presentation.generated.resources.clothing
import monuver.core.presentation.generated.resources.commission
import monuver.core.presentation.generated.resources.community
import monuver.core.presentation.generated.resources.concert_festival
import monuver.core.presentation.generated.resources.course
import monuver.core.presentation.generated.resources.credit_card
import monuver.core.presentation.generated.resources.custom
import monuver.core.presentation.generated.resources.dec
import monuver.core.presentation.generated.resources.december
import monuver.core.presentation.generated.resources.digital_subscription
import monuver.core.presentation.generated.resources.doctor
import monuver.core.presentation.generated.resources.donation
import monuver.core.presentation.generated.resources.drink
import monuver.core.presentation.generated.resources.e_wallet
import monuver.core.presentation.generated.resources.education
import monuver.core.presentation.generated.resources.education_fee
import monuver.core.presentation.generated.resources.electricity
import monuver.core.presentation.generated.resources.electronics
import monuver.core.presentation.generated.resources.entertainment
import monuver.core.presentation.generated.resources.expense
import monuver.core.presentation.generated.resources.feb
import monuver.core.presentation.generated.resources.february
import monuver.core.presentation.generated.resources.food
import monuver.core.presentation.generated.resources.food_beverages
import monuver.core.presentation.generated.resources.fuel
import monuver.core.presentation.generated.resources.furniture
import monuver.core.presentation.generated.resources.games
import monuver.core.presentation.generated.resources.gas
import monuver.core.presentation.generated.resources.grocery
import monuver.core.presentation.generated.resources.gym
import monuver.core.presentation.generated.resources.health_personal_care
import monuver.core.presentation.generated.resources.hobbies_collections
import monuver.core.presentation.generated.resources.hospital
import monuver.core.presentation.generated.resources.house
import monuver.core.presentation.generated.resources.ic_account_balance
import monuver.core.presentation.generated.resources.ic_apparel
import monuver.core.presentation.generated.resources.ic_attach_money
import monuver.core.presentation.generated.resources.ic_backpack
import monuver.core.presentation.generated.resources.ic_bar_chart
import monuver.core.presentation.generated.resources.ic_book
import monuver.core.presentation.generated.resources.ic_car_gear
import monuver.core.presentation.generated.resources.ic_car_repair
import monuver.core.presentation.generated.resources.ic_chair
import monuver.core.presentation.generated.resources.ic_clinical_notes
import monuver.core.presentation.generated.resources.ic_coffee
import monuver.core.presentation.generated.resources.ic_compare_arrows
import monuver.core.presentation.generated.resources.ic_credit_card
import monuver.core.presentation.generated.resources.ic_devices
import monuver.core.presentation.generated.resources.ic_directions_bus
import monuver.core.presentation.generated.resources.ic_electric_bolt
import monuver.core.presentation.generated.resources.ic_eyeglasses
import monuver.core.presentation.generated.resources.ic_festival
import monuver.core.presentation.generated.resources.ic_finance_mode
import monuver.core.presentation.generated.resources.ic_fitness_center
import monuver.core.presentation.generated.resources.ic_fork_spoon
import monuver.core.presentation.generated.resources.ic_groups
import monuver.core.presentation.generated.resources.ic_health_and_beauty
import monuver.core.presentation.generated.resources.ic_home_health
import monuver.core.presentation.generated.resources.ic_house
import monuver.core.presentation.generated.resources.ic_interactive_space
import monuver.core.presentation.generated.resources.ic_interests
import monuver.core.presentation.generated.resources.ic_local_gas_station
import monuver.core.presentation.generated.resources.ic_local_laundry_service
import monuver.core.presentation.generated.resources.ic_manga
import monuver.core.presentation.generated.resources.ic_massage
import monuver.core.presentation.generated.resources.ic_more_horiz
import monuver.core.presentation.generated.resources.ic_movie
import monuver.core.presentation.generated.resources.ic_payments
import monuver.core.presentation.generated.resources.ic_pill
import monuver.core.presentation.generated.resources.ic_podiatry
import monuver.core.presentation.generated.resources.ic_print
import monuver.core.presentation.generated.resources.ic_propane_tank
import monuver.core.presentation.generated.resources.ic_request_quote
import monuver.core.presentation.generated.resources.ic_savings_filled
import monuver.core.presentation.generated.resources.ic_savings_in
import monuver.core.presentation.generated.resources.ic_savings_out
import monuver.core.presentation.generated.resources.ic_school
import monuver.core.presentation.generated.resources.ic_search_hands_free
import monuver.core.presentation.generated.resources.ic_shield_with_heart
import monuver.core.presentation.generated.resources.ic_shopping_basket
import monuver.core.presentation.generated.resources.ic_spa
import monuver.core.presentation.generated.resources.ic_sports_esports
import monuver.core.presentation.generated.resources.ic_star
import monuver.core.presentation.generated.resources.ic_subscriptions
import monuver.core.presentation.generated.resources.ic_transportation
import monuver.core.presentation.generated.resources.ic_volunteer_activism
import monuver.core.presentation.generated.resources.ic_wallet
import monuver.core.presentation.generated.resources.ic_water_drop
import monuver.core.presentation.generated.resources.ic_water_full
import monuver.core.presentation.generated.resources.ic_wifi
import monuver.core.presentation.generated.resources.ic_work
import monuver.core.presentation.generated.resources.income
import monuver.core.presentation.generated.resources.insurance
import monuver.core.presentation.generated.resources.internet
import monuver.core.presentation.generated.resources.investment
import monuver.core.presentation.generated.resources.investment_result
import monuver.core.presentation.generated.resources.jan
import monuver.core.presentation.generated.resources.january
import monuver.core.presentation.generated.resources.jul
import monuver.core.presentation.generated.resources.july
import monuver.core.presentation.generated.resources.jun
import monuver.core.presentation.generated.resources.june
import monuver.core.presentation.generated.resources.laundry
import monuver.core.presentation.generated.resources.maintenance
import monuver.core.presentation.generated.resources.mar
import monuver.core.presentation.generated.resources.march
import monuver.core.presentation.generated.resources.massage
import monuver.core.presentation.generated.resources.may_full
import monuver.core.presentation.generated.resources.may_short
import monuver.core.presentation.generated.resources.medicine
import monuver.core.presentation.generated.resources.monthly
import monuver.core.presentation.generated.resources.nov
import monuver.core.presentation.generated.resources.november
import monuver.core.presentation.generated.resources.oct
import monuver.core.presentation.generated.resources.october
import monuver.core.presentation.generated.resources.online_ride
import monuver.core.presentation.generated.resources.other_expense
import monuver.core.presentation.generated.resources.other_income
import monuver.core.presentation.generated.resources.personal_care
import monuver.core.presentation.generated.resources.print_copy
import monuver.core.presentation.generated.resources.public_transport
import monuver.core.presentation.generated.resources.salary
import monuver.core.presentation.generated.resources.savings_complete
import monuver.core.presentation.generated.resources.savings_in
import monuver.core.presentation.generated.resources.savings_out
import monuver.core.presentation.generated.resources.sep
import monuver.core.presentation.generated.resources.september
import monuver.core.presentation.generated.resources.shoes
import monuver.core.presentation.generated.resources.shopping
import monuver.core.presentation.generated.resources.spa
import monuver.core.presentation.generated.resources.tax
import monuver.core.presentation.generated.resources.tea_coffee
import monuver.core.presentation.generated.resources.transfer
import monuver.core.presentation.generated.resources.transfer_account
import monuver.core.presentation.generated.resources.transportation
import monuver.core.presentation.generated.resources.vehicle
import monuver.core.presentation.generated.resources.water
import monuver.core.presentation.generated.resources.weekly
import monuver.core.presentation.generated.resources.yearly
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

object DatabaseCodeMapper {
    fun toAccountType(code: Int): StringResource {
        return when (code) {
            AccountType.CASH -> Res.string.cash
            AccountType.BANK -> Res.string.bank
            AccountType.E_WALLET -> Res.string.e_wallet
            AccountType.CREDIT_CARD -> Res.string.credit_card
            AccountType.INVESTMENT -> Res.string.investment
            else -> Res.string.all
        }
    }

    fun toAccountTypeIcon(code: Int): DrawableResource {
        return when (code) {
            AccountType.CASH -> Res.drawable.ic_payments
            AccountType.BANK -> Res.drawable.ic_account_balance
            AccountType.E_WALLET -> Res.drawable.ic_wallet
            AccountType.CREDIT_CARD -> Res.drawable.ic_credit_card
            AccountType.INVESTMENT -> Res.drawable.ic_bar_chart
            else -> Res.drawable.ic_account_balance
        }
    }

    fun toAccountTypeColor(code: Int): Color {
        return when (code) {
            AccountType.CASH -> Green400
            AccountType.BANK -> Indigo400
            AccountType.E_WALLET -> Blue400
            AccountType.CREDIT_CARD -> Brown400
            AccountType.INVESTMENT -> Amber500
            else -> Color.White
        }
    }

    fun toTransactionType(code: Int): StringResource {
        return when (code) {
            TransactionType.ALL -> Res.string.all
            TransactionType.INCOME -> Res.string.income
            TransactionType.EXPENSE -> Res.string.expense
            TransactionType.TRANSFER -> Res.string.transfer
            else -> Res.string.all
        }
    }

    fun toParentCategoryTitle(code: Int): StringResource {
        return when (code) {
            TransactionParentCategory.SALARY -> Res.string.salary
            TransactionParentCategory.BONUSES -> Res.string.bonuses
            TransactionParentCategory.COMMISSION -> Res.string.commission
            TransactionParentCategory.INVESTMENT_RESULT -> Res.string.investment_result
            TransactionParentCategory.OTHER_INCOME -> Res.string.other_income
            TransactionParentCategory.FOOD_BEVERAGES -> Res.string.food_beverages
            TransactionParentCategory.BILLS_UTILITIES -> Res.string.bills_utilities
            TransactionParentCategory.TRANSPORTATION -> Res.string.transportation
            TransactionParentCategory.HEALTH_PERSONAL_CARE -> Res.string.health_personal_care
            TransactionParentCategory.EDUCATION -> Res.string.education
            TransactionParentCategory.SHOPPING -> Res.string.shopping
            TransactionParentCategory.ENTERTAINMENT -> Res.string.entertainment
            TransactionParentCategory.OTHER_EXPENSE -> Res.string.other_expense
            TransactionParentCategory.TRANSFER -> Res.string.transfer
            else -> Res.string.income
        }
    }

    fun toChildCategoryTitle(code: Int): StringResource {
        return when (code) {
            TransactionChildCategory.SALARY -> Res.string.salary
            TransactionChildCategory.BONUSES -> Res.string.bonuses
            TransactionChildCategory.COMMISSION -> Res.string.commission
            TransactionChildCategory.INVESTMENT_RESULT -> Res.string.investment_result
            TransactionChildCategory.OTHER_INCOME -> Res.string.other_income

            TransactionChildCategory.FOOD -> Res.string.food
            TransactionChildCategory.DRINK -> Res.string.drink
            TransactionChildCategory.TEA_COFFEE -> Res.string.tea_coffee
            TransactionChildCategory.GROCERY -> Res.string.grocery

            TransactionChildCategory.WATER -> Res.string.water
            TransactionChildCategory.ELECTRICITY -> Res.string.electricity
            TransactionChildCategory.GAS -> Res.string.gas
            TransactionChildCategory.TAX -> Res.string.tax
            TransactionChildCategory.HOUSE -> Res.string.house
            TransactionChildCategory.INTERNET -> Res.string.internet

            TransactionChildCategory.MAINTENANCE -> Res.string.maintenance
            TransactionChildCategory.FUEL -> Res.string.fuel
            TransactionChildCategory.VEHICLE_ACCESSORIES -> Res.string.accessories
            TransactionChildCategory.ONLINE_RIDE -> Res.string.online_ride
            TransactionChildCategory.PUBLIC_TRANSPORT -> Res.string.public_transport

            TransactionChildCategory.HOSPITAL -> Res.string.hospital
            TransactionChildCategory.DOCTOR -> Res.string.doctor
            TransactionChildCategory.MEDICINE -> Res.string.medicine
            TransactionChildCategory.PERSONAL_CARE -> Res.string.personal_care
            TransactionChildCategory.MASSAGE -> Res.string.massage
            TransactionChildCategory.SPA -> Res.string.spa
            TransactionChildCategory.GYM -> Res.string.gym
            TransactionChildCategory.LAUNDRY -> Res.string.laundry

            TransactionChildCategory.EDUCATION_FEE -> Res.string.education_fee
            TransactionChildCategory.BOOKS_STATIONERY -> Res.string.books_stationery
            TransactionChildCategory.COURSE -> Res.string.course
            TransactionChildCategory.PRINT_COPY -> Res.string.print_copy

            TransactionChildCategory.CLOTHING -> Res.string.clothing
            TransactionChildCategory.SHOES -> Res.string.shoes
            TransactionChildCategory.BAG -> Res.string.bag
            TransactionChildCategory.ACCESSORIES -> Res.string.accessories
            TransactionChildCategory.ELECTRONICS -> Res.string.electronics
            TransactionChildCategory.FURNITURE -> Res.string.furniture
            TransactionChildCategory.VEHICLE -> Res.string.vehicle

            TransactionChildCategory.DIGITAL_SUBSCRIPTION -> Res.string.digital_subscription
            TransactionChildCategory.CINEMA -> Res.string.cinema
            TransactionChildCategory.GAMES -> Res.string.games
            TransactionChildCategory.CONCERT_FESTIVAL -> Res.string.concert_festival
            TransactionChildCategory.BOOKS_COMICS -> Res.string.books_comics
            TransactionChildCategory.HOBBIES_COLLECTIONS -> Res.string.hobbies_collections
            TransactionChildCategory.COMMUNITY -> Res.string.community

            TransactionChildCategory.DONATION -> Res.string.donation
            TransactionChildCategory.INSURANCE -> Res.string.insurance
            TransactionChildCategory.INVESTMENT -> Res.string.investment
            TransactionChildCategory.OTHER_EXPENSE -> Res.string.other_expense
            TransactionChildCategory.SAVINGS_COMPLETE -> Res.string.savings_complete

            TransactionChildCategory.TRANSFER_ACCOUNT -> Res.string.transfer_account
            TransactionChildCategory.SAVINGS_IN -> Res.string.savings_in
            TransactionChildCategory.SAVINGS_OUT -> Res.string.savings_out

            else -> Res.string.all
        }
    }

    fun toChildCategoryIcon(code: Int): DrawableResource {
        return when (code) {
            TransactionChildCategory.SALARY -> Res.drawable.ic_work
            TransactionChildCategory.BONUSES -> Res.drawable.ic_star
            TransactionChildCategory.COMMISSION -> Res.drawable.ic_attach_money
            TransactionChildCategory.INVESTMENT_RESULT -> Res.drawable.ic_finance_mode
            TransactionChildCategory.OTHER_INCOME -> Res.drawable.ic_more_horiz

            TransactionChildCategory.FOOD -> Res.drawable.ic_fork_spoon
            TransactionChildCategory.DRINK -> Res.drawable.ic_water_full
            TransactionChildCategory.TEA_COFFEE -> Res.drawable.ic_coffee
            TransactionChildCategory.GROCERY -> Res.drawable.ic_shopping_basket

            TransactionChildCategory.WATER -> Res.drawable.ic_water_drop
            TransactionChildCategory.ELECTRICITY -> Res.drawable.ic_electric_bolt
            TransactionChildCategory.GAS -> Res.drawable.ic_propane_tank
            TransactionChildCategory.TAX -> Res.drawable.ic_request_quote
            TransactionChildCategory.HOUSE -> Res.drawable.ic_house
            TransactionChildCategory.INTERNET -> Res.drawable.ic_wifi

            TransactionChildCategory.MAINTENANCE -> Res.drawable.ic_car_repair
            TransactionChildCategory.FUEL -> Res.drawable.ic_local_gas_station
            TransactionChildCategory.VEHICLE_ACCESSORIES -> Res.drawable.ic_car_gear
            TransactionChildCategory.ONLINE_RIDE -> Res.drawable.ic_transportation
            TransactionChildCategory.PUBLIC_TRANSPORT -> Res.drawable.ic_directions_bus

            TransactionChildCategory.HOSPITAL -> Res.drawable.ic_home_health
            TransactionChildCategory.DOCTOR -> Res.drawable.ic_clinical_notes
            TransactionChildCategory.MEDICINE -> Res.drawable.ic_pill
            TransactionChildCategory.PERSONAL_CARE -> Res.drawable.ic_health_and_beauty
            TransactionChildCategory.MASSAGE -> Res.drawable.ic_massage
            TransactionChildCategory.SPA -> Res.drawable.ic_spa
            TransactionChildCategory.GYM -> Res.drawable.ic_fitness_center
            TransactionChildCategory.LAUNDRY -> Res.drawable.ic_local_laundry_service

            TransactionChildCategory.EDUCATION_FEE -> Res.drawable.ic_school
            TransactionChildCategory.BOOKS_STATIONERY -> Res.drawable.ic_book
            TransactionChildCategory.COURSE -> Res.drawable.ic_interactive_space
            TransactionChildCategory.PRINT_COPY -> Res.drawable.ic_print

            TransactionChildCategory.CLOTHING -> Res.drawable.ic_apparel
            TransactionChildCategory.SHOES -> Res.drawable.ic_podiatry
            TransactionChildCategory.BAG -> Res.drawable.ic_backpack
            TransactionChildCategory.ACCESSORIES -> Res.drawable.ic_eyeglasses
            TransactionChildCategory.ELECTRONICS -> Res.drawable.ic_devices
            TransactionChildCategory.FURNITURE -> Res.drawable.ic_chair
            TransactionChildCategory.VEHICLE -> Res.drawable.ic_search_hands_free

            TransactionChildCategory.DIGITAL_SUBSCRIPTION -> Res.drawable.ic_subscriptions
            TransactionChildCategory.CINEMA -> Res.drawable.ic_movie
            TransactionChildCategory.GAMES -> Res.drawable.ic_sports_esports
            TransactionChildCategory.CONCERT_FESTIVAL -> Res.drawable.ic_festival
            TransactionChildCategory.BOOKS_COMICS -> Res.drawable.ic_manga
            TransactionChildCategory.HOBBIES_COLLECTIONS -> Res.drawable.ic_interests
            TransactionChildCategory.COMMUNITY -> Res.drawable.ic_groups

            TransactionChildCategory.DONATION -> Res.drawable.ic_volunteer_activism
            TransactionChildCategory.INSURANCE -> Res.drawable.ic_shield_with_heart
            TransactionChildCategory.INVESTMENT -> Res.drawable.ic_finance_mode
            TransactionChildCategory.OTHER_EXPENSE -> Res.drawable.ic_more_horiz
            TransactionChildCategory.SAVINGS_COMPLETE -> Res.drawable.ic_savings_filled

            TransactionChildCategory.TRANSFER_ACCOUNT -> Res.drawable.ic_compare_arrows
            TransactionChildCategory.SAVINGS_IN -> Res.drawable.ic_savings_in
            TransactionChildCategory.SAVINGS_OUT -> Res.drawable.ic_savings_out

            else -> Res.drawable.ic_account_balance
        }
    }

    fun toParentCategoryIconBackground(code: Int): Color {
        return when (code) {
            1 -> Green600
            2 -> Green400
            3 -> Teal400
            4 -> Teal600
            5 -> Green800
            6 -> Orange600
            7 -> Indigo400
            8 -> BlueGrey400
            9 -> Blue400
            10 -> Amber500
            11 -> Pink400
            12 -> DeepPurple400
            13 -> Brown400
            else -> Blue800
        }
    }

    fun toFullMonth(code: Int): StringResource {
        return when (code) {
            1 -> Res.string.january
            2 -> Res.string.february
            3 -> Res.string.march
            4 -> Res.string.april
            5 -> Res.string.may_full
            6 -> Res.string.june
            7 -> Res.string.july
            8 -> Res.string.august
            9 -> Res.string.september
            10 -> Res.string.october
            11 -> Res.string.november
            12 -> Res.string.december
            else -> Res.string.all
        }
    }

    fun toShortMonth(code: Int): StringResource {
        return when (code) {
            Month.ALL -> Res.string.all
            Month.JANUARY -> Res.string.jan
            Month.FEBRUARY -> Res.string.feb
            Month.MARCH -> Res.string.mar
            Month.APRIL -> Res.string.apr
            Month.MAY -> Res.string.may_short
            Month.JUNE -> Res.string.jun
            Month.JULY -> Res.string.jul
            Month.AUGUST -> Res.string.aug
            Month.SEPTEMBER -> Res.string.sep
            Month.OCTOBER -> Res.string.oct
            Month.NOVEMBER -> Res.string.nov
            Month.DECEMBER -> Res.string.dec
            else -> Res.string.all
        }
    }

    fun toCycle(code: Int): StringResource {
        return when (code) {
            Cycle.YEARLY -> Res.string.yearly
            Cycle.MONTHLY -> Res.string.monthly
            Cycle.WEEKLY -> Res.string.weekly
            else -> Res.string.custom
        }
    }
}