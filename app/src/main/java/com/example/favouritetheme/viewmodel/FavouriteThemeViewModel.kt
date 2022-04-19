package com.example.favouritetheme.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.util.stream.Collectors
import javax.inject.Inject

@HiltViewModel
class FavouriteThemeViewModel @Inject constructor() : ViewModel() {

    private val _favouriteTheme = MutableLiveData<String>()
    private val _usersWhoVotedForTheFavouriteTheme = MutableLiveData<List<String>>()

    fun onFavouriteThemeUpdated(): LiveData<String> = _favouriteTheme
    fun usersWhoVotedForTheFavouriteTheme(): LiveData<List<String>> = _usersWhoVotedForTheFavouriteTheme

    @RequiresApi(Build.VERSION_CODES.N)
    fun getThemes(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favouritesOutput: String
                val favouritesInputStream: InputStream = context.assets.open("favourites.txt")
                val size: Int = favouritesInputStream.available()
                val buffer = ByteArray(size)
                favouritesInputStream.read(buffer)
                favouritesOutput = String(buffer)

                val favouriteThemesMap = mapOf<String, String?>().toMutableMap()
                favouritesOutput.lines().forEach { it ->
                    if (it.isNotBlank()) {
                        it.trim().split(" ".toRegex(), 2).toTypedArray().let {
                            favouriteThemesMap[it[0]] = it[1]
                        }
                    }
                }

                val valueMap: Map<String, List<String>> =
                    favouriteThemesMap.keys.stream().collect(Collectors.groupingBy { k ->
                        favouriteThemesMap[k]
                    })

                val favouriteColorEntry = valueMap.maxByOrNull { it.value.size }
                withContext(Dispatchers.Main){
                    _favouriteTheme.postValue(favouriteColorEntry?.key)
                }

                val usersMap = usersMap(context)
                val userIdentifiers = favouriteColorEntry?.value
                val usersList = listOf<String>().toMutableList()

                userIdentifiers?.forEach { identifier ->
                    usersMap.forEach { userEntry ->
                        if (identifier == userEntry.key) {
                            userEntry.value?.let { usersList.add(it) }
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    _usersWhoVotedForTheFavouriteTheme.postValue(usersList)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun usersMap(context: Context): MutableMap<String, String?> {
        val usersOutput: String
        val usersInputStream: InputStream = context.assets.open("users.txt")
        val usersSize: Int = usersInputStream.available()
        val usersBuffer = ByteArray(usersSize)
        usersInputStream.read(usersBuffer)
        usersOutput = String(usersBuffer)

        val usersMap = mapOf<String, String?>().toMutableMap()
        usersOutput.lines().forEach { it ->
            if (it.isNotBlank()) {
                it.trim().split("\\s+".toRegex(), 2).toTypedArray().let {
                    usersMap[it[0]] = it[1].trim()
                }
            }
        }
        return usersMap
    }
}
