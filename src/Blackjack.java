/*	
 * 	File:				Blackjack.java
 * 	Associated Files:	Main.java, Deck.java, Card.java
 * 	Packages Needed:	java.util.ArrayList, java.util.HashMap, java.util.Scanner
 * 	Author:            	Michael Ngo (https://github.com/yeeshue99)
 * 	Date Modified:      8/18/2020 by Michael Ngo
 * 	Modified By:        Michael Ngo
 * 
 * 	Purpose:			Underlying structure for War card game
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*
 * Class:				Blackjack
 * Purpose:				Handles Blackjack engine and game
 * Methods:				PlayGame, GetDiscardChoice, GetActionChoice, NextPlayer
 */
public class Blackjack {

	int numPlayers = 2;
	int rounds = 0;
	Deck deck;
	ArrayList<ArrayList<Card>> allHands;
	String[] actions = { "hit", "stay"};
	String[] drawLocations = { "discard", "deck" };

	/*
	 * Function:			Initialize
	 * Params: 				Number of players(int)
	 * Purpose:				Initializes Blackjack engine
	 * Returns: 			
	 */
	public Blackjack(int numPlayers) {
		if (numPlayers <= 1) {
			numPlayers = 2;
			System.out.println("There has to be at least one player. I assume that's what you meant!");
		}
		this.numPlayers = numPlayers;
		deck = new Deck();
		System.out.println("Dealing the deck evenly to every player...");
		allHands = Deck.DealCards(numPlayers);
		for (int i = 0; i < numPlayers; i++) {
			if(i == 0) {
				System.out.println("The dealer's cards are:");
			}
			else {
				System.out.printf("Player #%d, this is the hand you were dealt:%n", (i + 1));
			}
			Deck.DisplayCards(allHands.get(i));
		}
	}

	/*
	 * Function:			PlayGame
	 * Params: 				Java command line input(Scanner)
	 * Purpose:				Run the game loop and communicate with user
	 * Returns: 			Player who won and the rounds the game took(int[])
	 */
	public void PlayGame(Scanner sc) {
		System.out.println("Welcome to the game of Blackjack!");
		
		for (int player = 0; player < numPlayers; player++){
			System.out.println("======================================");
			if(player == 0) {
				System.out.println("These are the dealer's cards:");
				Deck.DisplayCards(allHands.get(player));
				continue;
			}
			System.out.printf("Player #%d, these are your cards:%n", (player));
			Deck.DisplayCards(allHands.get(player));

			ChoiceLoop(sc, player);
		}
		
		System.out.println("Now the Dealer will begin their turn.");

		if(Deck.CalculateHandValue(allHands.get(0)) >= 17) {
			System.out.println("The Dealer already has at least 17 so they stay.");
		}
		while(Deck.CalculateHandValue(allHands.get(0)) < 17) {
			allHands.get(0).add(Deck.DrawCard());
			System.out.printf("This is their hand now:%n");
			Deck.DisplayCards(allHands.get(0));
			if (Deck.CalculateHandValue(allHands.get(0)) > 21) {
				System.out.println("The dealer busted!");
			}
		}

		
		int maxVal = -1;
		int maxPlayer = -1;
		int num21 = 0;
		
		for (int player  = 0; player < numPlayers; player++) {
			int currVal = Deck.CalculateHandValue(allHands.get(player));
			if(currVal == 21) {
				num21++;
			}
			if (currVal > maxVal && currVal <= 21) {
				maxVal = currVal;
				maxPlayer = player;
			}
		}
		if (num21 > 1) {
			System.out.print("Multiple players got 21!");
			for (int i = 0; i < numPlayers; i++) {
				if (Deck.CalculateHandValue(allHands.get(i)) == 21) {
					if (i == 0) {
						System.out.print("the Dealer, ");
					}
					System.out.printf("Player #%d, ", i);
				}
			}
		}
		else {
			if (maxPlayer == 0) {
				System.out.println("The Dealer won!");
			}
			else {
				System.out.printf("Player #%d won with their hand of:%n%s", maxPlayer, allHands.get(maxPlayer));
			}	
		}
	}

	/*
	 * Function:			ChoiceLoop
	 * Params: 				Java command line input(Scanner), Current player(int)
	 * Purpose:				Run turn loop of an individual player
	 * Returns: 			
	 */
	private void ChoiceLoop(Scanner sc, int player) {
		String action = "";
		
		System.out.println("Do you want to hit or stay? (To see list of actions type \"help\"):");
		while (!action.equalsIgnoreCase("stay")) {
			action = GetActionChoice(sc);
			if (action.equalsIgnoreCase(actions[0])) {
				allHands.get(player).add(Deck.DrawCard());
				System.out.printf("This is your hand now:%n");
				Deck.DisplayCards(allHands.get(player));
				if (Deck.CalculateHandValue(allHands.get(player)) > 21) {
					System.out.println("Oh no you went over 21!");
					action = "stay";
				}
				else if(Deck.CalculateHandValue(allHands.get(player)) == 21) {
					System.out.println("You got 21! Nice!");
					action = "stay";
				}
			}
			else if (action.equalsIgnoreCase(actions[1])) {
				continue;
			}
			else {
				System.out.println("The actions you can take are: ");
				System.out.println(Arrays.toString(actions));
			}
		}
	}
	
	/*
	 * Function:			GetActionChoice
	 * Params: 				Java command line input(Scanner)
	 * Purpose:				Communicates with user to find what action to do
	 * Returns: 			Player's chosen action(String)
	 */
	private String GetActionChoice(Scanner sc) {
		String action;
		action = sc.next();
		boolean reDoChoice = true;
		for (int i = 0; i < actions.length; i++) {
			if (action.equalsIgnoreCase(actions[i])) {
				reDoChoice = false;
			}
		}
		while (reDoChoice) {
			System.out.println("The actions you can take are: ");
			System.out.println(Arrays.toString(actions));
			action = sc.next();
			for (int i = 0; i < actions.length; i++) {
				if (action.equalsIgnoreCase(actions[i])) {
					reDoChoice = false;
				}
			}
		}
		return action;
	}
}
