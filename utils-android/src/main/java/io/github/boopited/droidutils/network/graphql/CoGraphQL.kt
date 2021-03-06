package io.github.boopited.droidutils.network.graphql

import android.content.Context
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import com.apollographql.apollo.fetcher.ResponseFetcher
import io.github.boopited.droidutils.network.graphql.impl.CoApolloGraphQL
import io.github.boopited.droidutils.network.graphql.model.ApolloConfig
import io.github.boopited.droidutils.network.graphql.model.NetworkResult
import okhttp3.Interceptor

interface CoGraphQL {

    /** Calls query on ApolloClient.
     * Catches errors and responds to expired sid.
     * This is the exposed entry point that repositories talk to when they need to make a query
     *
     * @param cachePolicy specify which cache policy should the query be built with
     * @see HttpCachePolicy
     *
     * @param responseFetcher specify which responseFetcher should the query be built with
     * (usually the same as cachePolicy)
     * @see ApolloResponseFetchers
     *
     * @return NetworkResult.Error of the cause or NetworkResult.Success with the correct data
     * @see NetworkResult */
    suspend fun <D : Operation.Data, T : Operation.Data, V : Operation.Variables> safeQuery(
        query: Query<D, T, V>,
        cachePolicy: HttpCachePolicy.Policy = HttpCachePolicy.NETWORK_ONLY,
        responseFetcher: ResponseFetcher = ApolloResponseFetchers.NETWORK_ONLY
    ): NetworkResult<T>


    /** Calls mutation on ApolloClient.
     * Catches errors and responds to expired sid.
     * This is the exposed entry point that repositories talk to when they need to make a mutation
     *
     * @return NetworkResult.Error of the cause or NetworkResult.Success with the correct data
     * @see NetworkResult */
    suspend fun <D : Operation.Data, T : Operation.Data, V : Operation.Variables> safeMutation(
        mutation: Mutation<D, T, V>
    ): NetworkResult<T>

    /** Called from logout.
     * Clears the normalized cache (on disk cache).
     * */
    fun clearData()

    companion object {
        fun get(context: Context, config: ApolloConfig,
                interceptors: List<Interceptor> = emptyList()): CoGraphQL
        {
            return CoApolloGraphQL(context, config, interceptors)
        }
    }
}