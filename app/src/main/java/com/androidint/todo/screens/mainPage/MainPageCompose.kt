package com.androidint.todo.screens.mainPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.androidint.todo.utils.ChartView
import com.androidint.todo.utils.TopAppBarCompose
import com.androidint.todo.utils.TopBarClickListener
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun MainPageCompose(
    navHostController: NavHostController
) {
    val topBarClickListener: TopBarClickListener
    val sdf = SimpleDateFormat("EEEE")
    val d = Date()
    val current_day: String = sdf.format(d)
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBarCompose(title = current_day)
        mainCard(
            inbox = "15",
            meetings = "10" ,
            trip = "0"
        )
        projectsList(5)


    }


}

@Composable
fun projectsList(
    count: Int
) {
    Column {
        Text(text = "Projects" ,
            Modifier
                .size(25.dp)
                .padding(25.dp) , Color.Black)
        LazyRow(contentPadding = PaddingValues(horizontal = 10.dp, vertical = 20.dp)) {
            items(count) { item ->
                Card {

                }
                Text(
                    text = "Item $item",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .background(color = androidx.compose.ui.graphics.Color.Gray)
                        .padding(16.dp)
                )
            }
        }
    }

}

@Composable
fun mainCard(
    inbox: String,
    meetings : String ,
    trip: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(15.dp),

    ) {
        Row (
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ChartView()
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Inbox               ${inbox}",
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Text(
                    text = "Meetings               ${meetings}",
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Text(
                    text = "Trip               ${trip}",
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

            }


        }
    }
}

