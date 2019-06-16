package com.mia.phase10.gameLogic;

import com.mia.phase10.classes.Card;
import com.mia.phase10.classes.Colour;
import com.mia.phase10.classes.SimpleCard;
import com.mia.phase10.classes.SpecialCard;
import com.mia.phase10.classes.SpecialCardValue;

import java.util.List;

public class CardEvaluator {
    private static CardEvaluator evaluatorInstance = new CardEvaluator();

    private CardEvaluator() {
    }

    public static CardEvaluator getInstance() {
        return evaluatorInstance;
    }


    public boolean checkPhase(Phase phase, List<Card> cardList1) {
        switch (phase) {
            case PHASE_4:
                return sevenInARow(cardList1);
            case PHASE_5:
                return eightInARow(cardList1);
            case PHASE_6:
                return nineInARow(cardList1);
            case PHASE_8:
                return checkIfPhaseEight(cardList1);
            default:return false;

        }

    }

    public boolean checkPhase(Phase phase, List<Card> cardList1, List<Card> cardList2) {
        switch (phase) {
            case PHASE_1:
                return checkIfPhaseOne(cardList1, cardList2);
            case PHASE_2:
                return checkIfPhaseTwo(cardList1, cardList2);
            case PHASE_3:
                return checkIfPhaseThree(cardList1, cardList2);
            case PHASE_7:
                return checkIfPhaseSeven(cardList1, cardList2);
            case PHASE_9:
                return checkIfPhaseNine(cardList1, cardList2);
            case PHASE_10:
                return checkIfPhaseTen(cardList1, cardList2);
            default:return false;
        }
    }

    private boolean isTwin(List<Card> list) {
        if (list.size() == 2) {
            return checkForEqualNumbers(list);
        }
        return false;
    }

    private boolean isTriplet(List<Card> list) {
        if (list.size() == 3) {
            return checkForEqualNumbers(list);
        }
        return false;
    }

    private boolean isQuad(List<Card> list) {
        if (list.size() == 4) {
            return checkForEqualNumbers(list);
        }
        return false;
    }

    private boolean isQuintuple(List<Card> list) {
        if (list.size() == 5) {
            return checkForEqualNumbers(list);
        }
        return false;
    }

    private boolean fourInARow(List<Card> list) {

        if (list.size() == 4) {
           return checkIfInARow(list);
        }
        return false;
    }

    private boolean sevenInARow(List<Card> list) {
        if (list.size() == 7) {
            return checkIfInARow(list);
        }
        return false;
    }

    private boolean eightInARow(List<Card> list) {
        if (list.size() == 8) {
            return checkIfInARow(list);
        }
        return false;
    }

    private boolean nineInARow(List<Card> list) {
        if (list.size() == 9) {
            return checkIfInARow(list);
        }
        return false;
    }

    private boolean checkIfPhaseOne(List<Card> list, List<Card> list2) {
        if (isTriplet(list) && isTriplet(list2)) {
            return true;
        }
        return false;
    }

    private boolean checkIfPhaseTwo(List<Card> list, List<Card> list2) {
        return ((isTriplet(list)&& fourInARow(list2))||(isTriplet(list2)&& fourInARow(list)));
    }

    private boolean checkIfPhaseThree(List<Card> list, List<Card> list2) {
        return ((isQuad(list)&& fourInARow(list2))||(isQuad(list2)&& fourInARow(list)));
    }

    private boolean checkIfPhaseSeven(List<Card> list, List<Card> list2) {
        if (isQuad(list) && isQuad(list2)) {
            return true;
        }
        return false;
    }

    private boolean checkIfPhaseEight(List<Card> list) {
        if (list.size() == 7) {
            return checkSameColors(list);
        }
        return false;
    }
    public boolean checkSameColors(List<Card> list){
        Colour col = null;
        for (Card c : list) {
            if (c instanceof SimpleCard) {
                if (col == null) {
                    col = ((SimpleCard) c).getColor();
                } else if (((SimpleCard) c).getColor() != col) {
                    return false;
                }
            } else if ((c instanceof SpecialCard) && ((SpecialCard) c).getValue() != SpecialCardValue.JOKER) {
                    return false;

            }
        }
        return true;
    }

    private boolean checkIfPhaseNine(List<Card> list, List<Card> list2) {
        return ((isQuintuple(list)&& isTwin(list2))||(isQuintuple(list2)&& isTwin(list)));
    }

    private boolean checkIfPhaseTen(List<Card> list, List<Card> list2) {
        return ((isQuintuple(list)&& isTriplet(list2))||(isQuintuple(list2)&& isTriplet(list)));
    }

    public boolean checkForEqualNumbers(List<Card> list) {
        int number = 0;
        for (Card c : list) {
            if (c instanceof SimpleCard) {
                if (number == 0) {
                    number = ((SimpleCard) c).getNumber();
                } else if (((SimpleCard) c).getNumber() != number) {
                    return false;
                }
            } else if ((c instanceof SpecialCard)&&((SpecialCard) c).getValue() != SpecialCardValue.JOKER) {
                    return false;

            }
        }
        return true;
    }

    public boolean checkIfInARow(List<Card> list){
        int number = 0;
        boolean firstNumber = false;

        for (Card c : list) {
            if (c instanceof SimpleCard) {
                if (!firstNumber) {
                    if (number < ((SimpleCard) c).getNumber()) {
                        number = ((SimpleCard) c).getNumber();
                        firstNumber=true;
                    } else {
                        return false;
                    }
                }else{
                    if ((number + 1 )== ((SimpleCard) c).getNumber()) {
                        number = ((SimpleCard) c).getNumber();
                    } else {
                        return false;
                    }
                }
            } else if (c instanceof SpecialCard) {
                if (((SpecialCard) c).getValue() == SpecialCardValue.JOKER) {
                    number++;
                } else {
                    return false;
                }

            } else {
                return false;
            }
        }
        return true;
    }
}
