package com.example.tictactoecompose

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoecompose.ui.theme.CadetBlue
import kotlin.collections.ArrayList

@SuppressLint("MutableCollectionMutableState")
@Composable
fun TicTacToe(modifier: Modifier = Modifier) {
    var tiles by remember {
        mutableStateOf(ArrayList<Tile>())
    }

    var winner by remember {
        mutableStateOf(Winner.Unknown)
    }

    if(tiles.size == 0) {
        for(i in 1..9) {
            tiles.add(Tile(isPlayer = false, isOpponent = false))
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when(winner) {
                Winner.Unknown -> Text(
                    text = "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Winner.Player -> Text(
                    text = "You win :)",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Winner.AI -> Text(
                    text = "You lose :(",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Winner.Draw -> Text(
                    text = "Draw",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tic Tac Toe Game",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                items(9) { i ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(100.dp)
                            .border(border = BorderStroke(3.dp, CadetBlue))
                            .clickable {
                                if (winner == Winner.Unknown) {
                                    tiles = tiles.mapIndexed { j, item ->
                                        if (i == j) {
                                            item.copy(isPlayer = true)
                                        } else {
                                            item
                                        }
                                    } as ArrayList<Tile>

                                    val ai = OpponentAI(tiles)
                                    if (ai.getWinner(tiles) != Winner.Player) {
                                        tiles = tiles.mapIndexed { j, item ->
                                            if (j == ai.bestMove) {
                                                item.copy(isOpponent = true)
                                            } else {
                                                item
                                            }
                                        } as ArrayList<Tile>
                                    }
                                    winner = ai.getWinner(tiles)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (tiles[i].isPlayer) {
                            Image(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(2.dp),
                                painter = painterResource(id = R.drawable.ic_outline_cross),
                                contentDescription = "symbol"
                            )
                        }
                        if(tiles[i].isOpponent) {
                            Image(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(2.dp),
                                painter = painterResource(id = R.drawable.ic_outline_circle_24),
                                contentDescription = "symbol"
                            )
                        }
                    }
                }
            }
            Button(
                modifier = Modifier.padding(top = 48.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = CadetBlue),
                onClick = {
                    winner = Winner.Unknown
                    tiles = tiles.map {
                        it.copy(isOpponent = false, isPlayer = false)
                    } as ArrayList<Tile>

            }) {
                Text(
                    text = "Reset Game",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }

}

