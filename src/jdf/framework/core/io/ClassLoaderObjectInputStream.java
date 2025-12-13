/*
 * @(#)ClassLoaderObjectInputStream.java
 *
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, 
 * you must contact to me.
 *
 * And, you should not remove the information about the copyright notice 
 * and the author.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author
 */
package jdf.framework.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;

public class ClassLoaderObjectInputStream extends ObjectInputStream
{

	private ClassLoader cLoader;

	public ClassLoaderObjectInputStream(final ClassLoader classLoader, final InputStream inputStream)
		throws IOException, StreamCorruptedException
	{
		super(inputStream);

		cLoader = classLoader;
	}

	protected Class resolveClass(final ObjectStreamClass objectStreamClass)
		throws IOException, ClassNotFoundException
	{
		final Class clazz = Class.forName(objectStreamClass.getName(), false, cLoader);

		if (null != clazz)
			return clazz;
		else
			return super.resolveClass(objectStreamClass);

	}
}