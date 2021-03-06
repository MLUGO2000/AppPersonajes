package com.example.apppersonajes.framework.retrofit.sources

import com.example.apppersonajes.BuildConfig
import com.example.apppersonajes.data.DetailCharacter.source.DataSourceDetailCharacter
import com.example.apppersonajes.domain.models.Character
import com.example.apppersonajes.domain.models.Comic
import com.example.apppersonajes.domain.models.Thumbnail
import com.example.apppersonajes.framework.retrofit.AuthenticationApi
import com.example.apppersonajes.framework.retrofit.models.ImageThumbnail
import com.example.apppersonajes.framework.retrofit.models.ServerCharacter
import com.example.apppersonajes.framework.retrofit.requests.CharacterDetail.CharacterDetailRequest
import com.example.apppersonajes.framework.translators.toDomainCharacter
import com.example.apppersonajes.framework.translators.toDomainComic
import com.example.apppersonajes.utils.DataResult
import com.example.apppersonajes.utils.safeApiCall
import java.io.IOException

class RemoteSourceCharacterDetail(private val request: CharacterDetailRequest) : DataSourceDetailCharacter {


    override suspend fun getCharacterById(idCharacter: Int): DataResult<Character> {
        return safeApiCall(
            call = { requestGetCharacterById(idCharacter) },
            errorMessage = "Error"
        )
    }

    override suspend fun getComicByCharacter(idCharacter: Int): DataResult<List<Comic>>{
        return safeApiCall(
            call = {requestGetComicByCharacter(idCharacter)},
            errorMessage = "Error"
        )
    }

    private suspend fun requestGetComicByCharacter(idCharacter: Int): DataResult<List<Comic>> {

        val auth=AuthenticationApi()

        val response=request.service.getComicByCharacter(idCharacter,BuildConfig.MarvelId,auth.time,auth.hash)

        if(response.isSuccessful){
            val result=response.body()?.comicResult?.dataResults

            if(result!=null){
                return DataResult.Success(result.map { it.toDomainComic() })
            }
        }
        return DataResult.Failure(IOException("Error Obteniedo Comics del Personaje"))
    }

    private suspend fun requestGetCharacterById(idCharacter: Int): DataResult<Character> {
        val auth=AuthenticationApi()

        val response=request.service.getDetailCharacterById(idCharacter,BuildConfig.MarvelId,auth.time,auth.hash)

        if (response.isSuccessful){
            val result=response.body()?.charactersResult?.dataResults?.get(0)

            if (result!=null){
                return DataResult.Success(result.toDomainCharacter())
            }
        }
        return DataResult.Failure(IOException("Error Obteniendo Detalles de Personaje"))
    }
}



