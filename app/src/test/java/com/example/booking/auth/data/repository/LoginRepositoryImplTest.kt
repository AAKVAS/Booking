package com.example.booking.auth.data.repository

import com.example.booking.auth.data.datasource.CloudLoginDatasource
import com.example.booking.auth.data.datasource.CloudLoginDatasourceMock
import com.example.booking.auth.data.datasource.LocalLoginDatasource
import com.example.booking.auth.data.datasource.LocalLoginDatasourceMock
import com.example.booking.auth.domain.model.LoginDetails
import com.example.booking.auth.domain.model.LoginResult
import com.example.booking.auth.domain.model.RegisterResult
import com.example.booking.auth.domain.model.RegistrationDetails
import com.example.booking.auth.domain.model.UserDetails
import com.example.booking.profile.domain.model.ChangePasswordResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class LoginRepositoryImplTest {

    private val defaultCloudDatasourceMock: CloudLoginDatasource
        get() = CloudLoginDatasourceMock()

    private val defaultLocalLoginDatasourceMock: LocalLoginDatasource
        get() {
            val mock = mockk<LocalLoginDatasource>()
            coEvery { mock.saveUserDetails(any()) } returns Unit
            return mock
        }

    @Test
    fun `login should return success when correct user params`() {
        val localLoginDatasource = defaultLocalLoginDatasourceMock
        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, defaultCloudDatasourceMock)
        runTest {
            val expected = LoginResult.Success(EXISTING_USER_DETAILS)
            val loginDetails = LoginDetails(EXISTING_USER_LOGIN, EXISTING_USER_PASSWORD)

            val result: LoginResult = loginRepositoryImpl.login(loginDetails)

            coVerify(exactly = 1) { localLoginDatasource.saveUserDetails(any()) }
            Assert.assertEquals(expected, result)
        }
    }

    @Test
    fun `login should return failure when wrong password`() {
        val localLoginDatasource = defaultLocalLoginDatasourceMock
        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, defaultCloudDatasourceMock)
        runTest {
            val loginDetails = LoginDetails(EXISTING_USER_LOGIN, WRONG_PASSWORD)
            val result: LoginResult = loginRepositoryImpl.login(loginDetails)

            coVerify(exactly = 0) { localLoginDatasource.saveUserDetails(any()) }
            Assert.assertTrue(result is LoginResult.Failure)
        }
    }

    @Test
    fun `login should return failure when wrong user name`() {
        val localLoginDatasource = defaultLocalLoginDatasourceMock
        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, defaultCloudDatasourceMock)
        runTest {
            val loginDetails = LoginDetails(NOT_EXISTING_USER_LOGIN, EXISTING_USER_PASSWORD)
            val result: LoginResult = loginRepositoryImpl.login(loginDetails)

            Assert.assertTrue(result is LoginResult.Failure)
        }
    }

    @Test
    fun `register should return new user details`() {
        val localLoginDatasource = defaultLocalLoginDatasourceMock
        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, defaultCloudDatasourceMock)
        runTest {
            val registrationDetails = RegistrationDetails(
                login = "drove",
                lastname = "lastname",
                firstname = "firstname",
                birthday = 0,
                password = "1111"
            )
            val result: RegisterResult = loginRepositoryImpl.register(registrationDetails)

            coVerify(exactly = 1) { localLoginDatasource.saveUserDetails(any()) }
            Assert.assertTrue(registrationDetails.corresponds(result))
        }
    }

    @Test
    fun `register should return failure for existing user`() {
        val localLoginDatasource = defaultLocalLoginDatasourceMock
        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, defaultCloudDatasourceMock)
        runTest {
            val registrationDetails = RegistrationDetails(
                login = EXISTING_USER_LOGIN,
                lastname = "lastname",
                firstname = "firstname",
                birthday = 0,
                password = ""
            )
            val result: RegisterResult = loginRepositoryImpl.register(registrationDetails)

            coVerify(exactly = 0) { localLoginDatasource.saveUserDetails(any()) }
            Assert.assertTrue(result is RegisterResult.Failure)
        }
    }

    @Test
    fun `isUserLogged returns true when user logged`() {
        val localLoginDatasource = LocalLoginDatasourceMock()
        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, defaultCloudDatasourceMock)

        runTest {
            val loginDetails = LoginDetails(EXISTING_USER_LOGIN, EXISTING_USER_PASSWORD)
            loginRepositoryImpl.login(loginDetails)

            Assert.assertTrue(loginRepositoryImpl.isUserLogged())
        }
    }

    @Test
    fun `isUserLogged returns false when user not logged`() {
        val localLoginDatasource = LocalLoginDatasourceMock()
        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, defaultCloudDatasourceMock)

        runTest {
            Assert.assertFalse(loginRepositoryImpl.isUserLogged())
        }
    }

    @Test
    fun `isUserLogged returns false when user logout`() {
        val localLoginDatasource = LocalLoginDatasourceMock()
        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, defaultCloudDatasourceMock)

        runTest {
            val loginDetails = LoginDetails(EXISTING_USER_LOGIN, EXISTING_USER_PASSWORD)
            loginRepositoryImpl.login(loginDetails)
            loginRepositoryImpl.logout()

            Assert.assertFalse(loginRepositoryImpl.isUserLogged())
        }
    }

    @Test
    fun `getUserDetails returns details after login`() {
        val localLoginDatasource = LocalLoginDatasourceMock()
        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, defaultCloudDatasourceMock)

        runTest {
            val loginDetails = LoginDetails(EXISTING_USER_LOGIN, EXISTING_USER_PASSWORD)
            loginRepositoryImpl.login(loginDetails)
            val userDetails: UserDetails = loginRepositoryImpl.getUserDetails().first()
            Assert.assertEquals(loginDetails.login, userDetails.login)
        }
    }

    @Test
    fun `getUserDetails falls when user logout`() {
        val localLoginDatasource = LocalLoginDatasourceMock()
        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, defaultCloudDatasourceMock)

        Assert.assertThrows(NullPointerException::class.java) {
            runTest {
                val loginDetails = LoginDetails(EXISTING_USER_LOGIN, EXISTING_USER_PASSWORD)
                loginRepositoryImpl.login(loginDetails)
                loginRepositoryImpl.logout()
                loginRepositoryImpl.getUserDetails().first()
            }
        }
    }

    @Test
    fun `getUserDetails falls when user not logged`() {
        val localLoginDatasource = LocalLoginDatasourceMock()
        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, defaultCloudDatasourceMock)

        Assert.assertThrows(NullPointerException::class.java) {
            runTest {
                loginRepositoryImpl.getUserDetails().first()
            }
        }
    }

    @Test
    fun `saveUserDetails changes user details`() {
        val localLoginDatasource = LocalLoginDatasourceMock()
        val cloudLoginDatasource = mockk<CloudLoginDatasource>()
        coEvery { cloudLoginDatasource.saveUserDetails(any()) } returns Result.success(Unit)

        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, cloudLoginDatasource)

        runTest {
            val userDetails = EXISTING_USER_DETAILS.copy(
                firstname = "new_firstname",
                lastname = "new_lastname",
                birthday = 100
            )
            loginRepositoryImpl.saveUserDetails(userDetails)
            coVerify(exactly = 1) { cloudLoginDatasource.saveUserDetails(any()) }

            val localUserDetails: UserDetails = loginRepositoryImpl.getUserDetails().first()

            Assert.assertTrue(userDetails.corresponds(localUserDetails))
        }
    }

    @Test
    fun `changePassword change user password`() {
        val localLoginDatasource = LocalLoginDatasourceMock()
        val cloudLoginDatasource = defaultCloudDatasourceMock

        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, cloudLoginDatasource)
        runTest {
            val newPassword = "new_password"
            val oldLoginDetails = LoginDetails(EXISTING_USER_LOGIN, EXISTING_USER_PASSWORD)

            loginRepositoryImpl.login(oldLoginDetails)
            val changePasswordResult: ChangePasswordResult =
                loginRepositoryImpl.changePassword(EXISTING_USER_PASSWORD, newPassword)

            Assert.assertTrue(changePasswordResult is ChangePasswordResult.Success)

            loginRepositoryImpl.logout()

            val newLoginDetails = LoginDetails(EXISTING_USER_LOGIN, newPassword)
            val loginResult: LoginResult = loginRepositoryImpl.login(newLoginDetails)

            val expected = LoginResult.Success(EXISTING_USER_DETAILS)

            Assert.assertEquals(expected, loginResult)
        }
    }

    @Test
    fun `old password doesn't work after changePassword`() {
        val localLoginDatasource = LocalLoginDatasourceMock()
        val cloudLoginDatasource = defaultCloudDatasourceMock

        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, cloudLoginDatasource)
        runTest {
            val newPassword = "new_password"
            val oldLoginDetails = LoginDetails(EXISTING_USER_LOGIN, EXISTING_USER_PASSWORD)

            loginRepositoryImpl.login(oldLoginDetails)
            loginRepositoryImpl.changePassword(EXISTING_USER_PASSWORD, newPassword)
            loginRepositoryImpl.logout()

            val loginResult: LoginResult = loginRepositoryImpl.login(oldLoginDetails)
            Assert.assertTrue(loginResult is LoginResult.Failure)
        }
    }

    @Test
    fun `changePassword returns wrongOldPassword when wrong oldPassword`() {
        val localLoginDatasource = LocalLoginDatasourceMock()
        val cloudLoginDatasource = defaultCloudDatasourceMock

        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, cloudLoginDatasource)
        runTest {
            val newPassword = "new_password"
            val oldLoginDetails = LoginDetails(EXISTING_USER_LOGIN, EXISTING_USER_PASSWORD)

            loginRepositoryImpl.login(oldLoginDetails)
            val result = loginRepositoryImpl.changePassword(WRONG_PASSWORD, newPassword)

            Assert.assertTrue(result is ChangePasswordResult.WrongOldPassword)
        }
    }

    @Test
    fun `changePassword return failure when not logged`() {
        val localLoginDatasource = LocalLoginDatasourceMock()
        val cloudLoginDatasource = defaultCloudDatasourceMock

        val loginRepositoryImpl = LoginRepositoryImpl(localLoginDatasource, cloudLoginDatasource)
        runTest {
            val newPassword = "new_password"
            val result = loginRepositoryImpl.changePassword(EXISTING_USER_PASSWORD, newPassword)

            Assert.assertTrue(result is ChangePasswordResult.Failure)
        }
    }

    private fun RegistrationDetails.corresponds(result: RegisterResult): Boolean {
        if (result is RegisterResult.Success) {
            val details = result.userDetails
            return this.login == details.login
                && this.firstname == details.firstname
                && this.lastname == details.lastname
                && this.birthday == details.birthday
        }
        return false
    }

    private fun UserDetails.corresponds(userDetails: UserDetails): Boolean {
        return this.login == userDetails.login
            && this.firstname == userDetails.firstname
            && this.lastname == userDetails.lastname
            && this.birthday == userDetails.birthday
    }

    companion object {
        const val EXISTING_USER_LOGIN = "plotan"
        const val EXISTING_USER_PASSWORD = "1111"
        const val WRONG_PASSWORD = "test"
        const val NOT_EXISTING_USER_LOGIN = "asdqwc3f4rd22"

        val EXISTING_USER_DETAILS = UserDetails(
            token = "test",
            login = EXISTING_USER_LOGIN,
            lastname = "lastname",
            firstname = "firstname",
            birthday = 0,
        )
    }
}