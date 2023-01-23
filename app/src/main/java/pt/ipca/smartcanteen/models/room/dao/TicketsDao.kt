package pt.ipca.smartcanteen.models.room.dao

import androidx.room.*
import pt.ipca.smartcanteen.models.room.tables.Tickets

@Dao
interface TicketsDao {
    @Query("SELECT * FROM Tickets")
    fun getAllTickets(): List<Tickets>

    @Query("SELECT * FROM Tickets WHERE ticketid=:ticketid")
    fun getTicket(vararg ticketid: String): Tickets?

    @Insert
    fun insertAll(vararg ticket: Tickets)

    @Update
    fun update(vararg ticket: Tickets)

    @Delete
    fun delete(vararg ticket: Tickets)
}